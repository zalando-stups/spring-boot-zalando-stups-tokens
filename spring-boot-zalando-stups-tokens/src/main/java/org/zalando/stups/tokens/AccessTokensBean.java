/**
 * Copyright (C) 2015 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.stups.tokens;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.zalando.stups.tokens.config.AccessTokensBeanProperties;
import org.zalando.stups.tokens.config.CircuitBreakerConfiguration;
import org.zalando.stups.tokens.config.TokenConfiguration;
import org.zalando.stups.tokens.mcb.MCBConfig;

/**
 * @author  jbellmann
 */
public class AccessTokensBean implements AccessTokens, SmartLifecycle, BeanFactoryAware {

    static final String OAUTH2_ACCESS_TOKENS = "OAUTH2_ACCESS_TOKENS";

    private final Logger logger = LoggerFactory.getLogger(AccessTokensBean.class);

    protected AccessTokens accessTokensDelegate;

    private final AccessTokensBeanProperties accessTokensBeanProperties;

    private volatile boolean running = false;

    private List<MetricsListener> metricsListeners = new ArrayList<MetricsListener>(0);

    public AccessTokensBean(final AccessTokensBeanProperties accessTokensBeanProperties) {
        this.accessTokensBeanProperties = accessTokensBeanProperties;
    }

    @Override
    public String get(final Object tokenId) throws AccessTokenUnavailableException {
        try {
            return accessTokensDelegate.get(tokenId);
        } catch (AccessTokenUnavailableException e) {
            logger.error("Token unavailable for service : {}", tokenId.toString());
            throw e;
        }
    }

    @Override
    public AccessToken getAccessToken(final Object tokenId) throws AccessTokenUnavailableException {
        try {

            return accessTokensDelegate.getAccessToken(tokenId);
        } catch (AccessTokenUnavailableException e) {
            logger.error("Token unavailable for service : {}", tokenId.toString());
            throw e;
        }
    }

    @Override
    public void invalidate(final Object tokenId) {
        accessTokensDelegate.invalidate(tokenId);
    }

    protected UserCredentialsProvider getUserCredentialsProvider() {

        return new JsonFileBackedUserCredentialsProvider(getCredentialsFile(
                    accessTokensBeanProperties.getUserCredentialsFilename()));
    }

    protected ClientCredentialsProvider getClientCredentialsProvider() {

        return new JsonFileBackedClientCredentialsProvider(getCredentialsFile(
                    accessTokensBeanProperties.getClientCredentialsFilename()));
    }

    protected File getCredentialsFile(final String credentialsFilename) {
        return new File(accessTokensBeanProperties.getCredentialsDirectory(), credentialsFilename);
    }

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setMetricsListeners(List<MetricsListener> metricsListeners) {
        Assert.notNull(metricsListeners, "metricsListeners-list should never be null");
        this.metricsListeners = metricsListeners;
    }

    @Override
    public synchronized void start() {
        if (isRunning()) {
            return;
        }
        
        logger.info("starting 'accessTokensBean' ...");

        AccessTokensBuilder builder = null;

        if (isTestingConfigured()) {
            logger.info("Test-Tokens will be used");

            builder = new FixedTokenAccessTokenBuilder(accessTokensBeanProperties.getAccessTokenUri(), getFixedToken());
        } else {

            // default
            builder = Tokens.createAccessTokensWithUri(accessTokensBeanProperties.getAccessTokenUri());
        }

        builder.usingClientCredentialsProvider(getClientCredentialsProvider());
        builder.usingUserCredentialsProvider(getUserCredentialsProvider());

        if (accessTokensBeanProperties.getTokenInfoUri() != null) {
            builder.tokenInfoUri(accessTokensBeanProperties.getTokenInfoUri());
        }

        configureTokenRefresherCircuitBreaker(builder);

        configureTokenVerifierCircuitBreaker(builder);

        if (!metricsListeners.isEmpty()) {
            builder.metricsListener(new CompositeMetricsListener(metricsListeners));
        }

        configureScheduler(builder);

        for (TokenConfiguration tc : accessTokensBeanProperties.getTokenConfigurationList()) {
            logger.info("configure scopes for service {}", tc.getTokenId());

            AccessTokenConfiguration configuration = builder.manageToken(tc.getTokenId());
            configuration.addScopes(new HashSet<Object>(tc.getScopes()));
        }

        // percentages
        builder.refreshPercentLeft(accessTokensBeanProperties.getRefreshPercentLeft());
        builder.warnPercentLeft(accessTokensBeanProperties.getWarnPercentLeft());

        // scheduling
        builder.schedulingPeriod(accessTokensBeanProperties.getSchedulingPeriod());
        builder.schedulingTimeUnit(accessTokensBeanProperties.getSchedulingTimeUnit());

        // tokenVerifier
        builder.tokenVerifierSchedulingPeriod(accessTokensBeanProperties.getTokenVerifierSchedulingPeriod());
        builder.tokenVerifierSchedulingTimeUnit(accessTokensBeanProperties.getTokenVerifierSchedulingTimeUnit());

        logger.info("Start 'accessTokenRefresher' ...");
        accessTokensDelegate = builder.start();
        running = true;
        logger.info("'accessTokenRefresher' started.");
        logger.info("'accessTokensBean' started.");
    }

    protected void configureTokenRefresherCircuitBreaker(AccessTokensBuilder builder) {
        CircuitBreakerConfiguration cbc = accessTokensBeanProperties.getTokenRefresherCircuitBreaker();
        MCBConfig config = new MCBConfig.Builder().withErrorThreshold(cbc.getErrorThreshold())
                .withTimeout(cbc.getTimeout()).withMaxMulti(cbc.getMaxMulti()).withTimeUnit(cbc.getTimeUnit()).build();
        builder.tokenRefresherMcbConfig(config);
    }

    protected void configureTokenVerifierCircuitBreaker(AccessTokensBuilder builder) {
        CircuitBreakerConfiguration cbc = accessTokensBeanProperties.getTokenVerifierCircuitBreaker();
        MCBConfig config = new MCBConfig.Builder().withErrorThreshold(cbc.getErrorThreshold())
                .withTimeout(cbc.getTimeout()).withMaxMulti(cbc.getMaxMulti()).withTimeUnit(cbc.getTimeUnit()).build();
        builder.tokenVerifierMcbConfig(config);
    }

    protected void configureScheduler(AccessTokensBuilder builder) {
        if (accessTokensBeanProperties.isUseExistingScheduler()) {
            TaskScheduler taskScheduler = null;
            try {
                taskScheduler = this.beanFactory.getBean(TaskScheduler.class);
            } catch (NoUniqueBeanDefinitionException e) {
                taskScheduler = this.beanFactory.getBean("taskScheduler", TaskScheduler.class);
            } catch (NoSuchBeanDefinitionException ex) {
                logger.warn("'useExistingScheduler' was configured to 'true', but we did not find any bean.");
            }
            if (taskScheduler != null) {
                if (taskScheduler instanceof ThreadPoolTaskScheduler) {
                    logger.info("use 'taskScheduler' from existing application-context");
                    builder.existingExecutorService(((ThreadPoolTaskScheduler) taskScheduler).getScheduledExecutor());
                } else if (taskScheduler instanceof ConcurrentTaskScheduler) {
                    logger.info("use 'taskScheduler' from existing application-context");
                    builder.existingExecutorService((ScheduledExecutorService) ((ConcurrentTaskScheduler) taskScheduler)
                            .getConcurrentExecutor());
                } else {
                    logger.info("no existing taskScheduler found, use defaults");
                }
            }
        }
    }

    protected final boolean isTestingConfigured() {
        if (StringUtils.hasText(System.getenv(OAUTH2_ACCESS_TOKENS))
                && StringUtils.hasText(accessTokensBeanProperties.getTestTokens())) {

            logger.warn(
                "'Test-Tokens' configured in yaml-file as also in ENV-VARIABLE 'OAUTH2_ACCESS_TOKENS' ! CONFIGURE ONLY ONE!");

            return true;

        } else if (StringUtils.hasText(System.getenv(OAUTH2_ACCESS_TOKENS))) {

            return true;
        } else if (StringUtils.hasText(accessTokensBeanProperties.getTestTokens())) {

            return true;
        }

        return false;
    }

    protected String getFixedToken() {
        String token = System.getenv(OAUTH2_ACCESS_TOKENS);
        if (StringUtils.hasText(token)) {
            return token;
        } else {
            return accessTokensBeanProperties.getTestTokens();
        }
    }

    @Override
    public synchronized void stop() {
        if (!isRunning()) {
            return;
        }

        logger.info("Stop 'accessTokenRefresher' ...");
        accessTokensDelegate.stop();
        running = false;
        logger.info("'accessTokenRefresher' stopped.");

    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {

        return accessTokensBeanProperties.getPhase();
    }

    @Override
    public boolean isAutoStartup() {

        return accessTokensBeanProperties.isAutoStartup();
    }

    @Override
    public void stop(final Runnable callback) {
        stop();
        callback.run();
    }
}
