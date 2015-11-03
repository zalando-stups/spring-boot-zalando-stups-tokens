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

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.SmartLifecycle;

import org.springframework.util.StringUtils;

import org.zalando.stups.tokens.config.AccessTokensBeanProperties;
import org.zalando.stups.tokens.config.TokenConfiguration;

/**
 * @author  jbellmann
 */
public class AccessTokensBean implements AccessTokens, SmartLifecycle {

    static final String OAUTH2_ACCESS_TOKENS = "OAUTH2_ACCESS_TOKENS";

    private final Logger logger = LoggerFactory.getLogger(AccessTokensBean.class);

    private AccessTokens accessTokensDelegate;

    private final AccessTokensBeanProperties accessTokensBeanProperties;

    private volatile boolean running = false;

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

        for (TokenConfiguration tc : accessTokensBeanProperties.getTokenConfigurationList()) {
            logger.info("configure scopes for service {}", tc.getTokenId());

            AccessTokenConfiguration configuration = builder.manageToken(tc.getTokenId());
            configuration.addScopes(new HashSet<Object>(tc.getScopes()));
        }

        logger.info("Start 'accessTokenRefresher' ...");
        accessTokensDelegate = builder.start();
        running = true;
        logger.info("'accessTokenRefresher' started.");
        logger.info("'accessTokensBean' started.");
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
