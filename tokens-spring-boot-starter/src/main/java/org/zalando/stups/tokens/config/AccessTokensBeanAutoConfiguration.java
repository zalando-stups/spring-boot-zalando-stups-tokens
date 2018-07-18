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
package org.zalando.stups.tokens.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.zalando.stups.tokens.AccessToken;
import org.zalando.stups.tokens.AccessTokenUnavailableException;
import org.zalando.stups.tokens.AccessTokens;
import org.zalando.stups.tokens.AccessTokensBean;
import org.zalando.stups.tokens.ClientCredentialsProvider;
import org.zalando.stups.tokens.JsonFileBackedClientCredentialsProvider;
import org.zalando.stups.tokens.MetricsListener;

/**
 * @author jbellmann
 */
@Configuration
@EnableConfigurationProperties({ AccessTokensBeanProperties.class })
public class AccessTokensBeanAutoConfiguration {

    private final Logger logger = LoggerFactory.getLogger(AccessTokensBeanAutoConfiguration.class);

    @Autowired
    private AccessTokensBeanProperties accessTokensBeanProperties;

    @Autowired(required = false)
    private List<MetricsListener> metricsListeners = new ArrayList<MetricsListener>(0);

    @Bean
    @ConditionalOnProperty(prefix = "tokens", name = "enable-mock", havingValue = "true")
    public AccessTokensBean mockAccessTokensBean(BeanFactory beanFactory) {
        return new MockAccessTokensBean(accessTokensBeanProperties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "tokens", name = "enable-mock", havingValue = "false", matchIfMissing=true)
    public AccessTokensBean accessTokensBean(BeanFactory beanFactory, Environment environment) {
        AccessTokensBean bean = new AccessTokensBean(accessTokensBeanProperties);
        bean.setBeanFactory(beanFactory);
        bean.setMetricsListeners(metricsListeners);
        if (accessTokensBeanProperties.isStartAfterCreation()) {
            logger.info("'accessTokensBean' was configured to 'startAfterCreation', starting now ...");
            bean.start();
        }
        return bean;
    }

    @Bean
    @ConditionalOnProperty(prefix = "tokens", name = "expose-client-credential-provider", havingValue = "true")
    public ClientCredentialsProvider clientCredentialsProvider() {
        return new JsonFileBackedClientCredentialsProvider(
                getCredentialsFile(accessTokensBeanProperties.getClientCredentialsFilename()));
    }

    protected File getCredentialsFile(final String credentialsFilename) {
        return new File(accessTokensBeanProperties.getCredentialsDirectory(), credentialsFilename);
    }

    static class MockAccessTokensBean extends AccessTokensBean {

        private final Logger logger = LoggerFactory.getLogger(MockAccessTokensBean.class);

        private static final String BEARER = "BEARER";
        private static final String MOCK_ENABLED_TOKEN = "MOCK_ENABLED_TOKEN";

        public MockAccessTokensBean(AccessTokensBeanProperties accessTokensBeanProperties) {
            super(accessTokensBeanProperties);
            accessTokensDelegate = new AccessTokens() {

                @Override
                public void stop() {
                }

                @Override
                public void invalidate(Object tokenId) {

                }

                @Override
                public AccessToken getAccessToken(Object tokenId) throws AccessTokenUnavailableException {
                    return new AccessToken(get("IGNORE"), BEARER, -1, validUntil());
                }

                @Override
                public String get(Object tokenId) throws AccessTokenUnavailableException {
                    if (isTestingConfigured()) {
                        return getFixedToken();
                    } else {
                        return MOCK_ENABLED_TOKEN;
                    }
                }

                private Date validUntil() {
                    final long expiresInSeconds = TimeUnit.DAYS.toSeconds(2);
                    return new Date(System.currentTimeMillis() + (expiresInSeconds * 1000));
                }
            };
        }

        @Override
        public synchronized void start() {
            logger.info("USING MOCK_ACCESS_TOKENS_BEAN, OAUTH WILL NOT WORK !!!");
        }

    }

}
