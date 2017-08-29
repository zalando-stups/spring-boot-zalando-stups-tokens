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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.util.StringUtils;
import org.zalando.stups.tokens.AccessToken;
import org.zalando.stups.tokens.AccessTokensBean;
import org.zalando.stups.tokens.ClientCredentialsProvider;

/**
 * @author jbellmann
 */
@ConfigurationProperties(prefix = "tokens")
public class AccessTokensBeanProperties {

    private static final String DEFAULT_CREDENTIALS_DIR = "/meta/credentials";

    private static final String OAUTH2_ACCESS_TOKEN_URL = "OAUTH2_ACCESS_TOKEN_URL";

    private static final String CREDENTIALS_DIR = "CREDENTIALS_DIR";

    private static final String CLIENT_JSON = "client.json";

    private static final String USER_JSON = "user.json";

    /**
     * The url to the access-token-endpoint.<br/>
     * e.g. https://auth.example.com/oauth2/access_token?realm=/yourrealm
     */
    private URI accessTokenUri = initializeAccessTokenUrlFromEnvironment();

    /**
     * Percentage when an {@link AccessToken} will be refreshed if possible.
     */
    private int refreshPercentLeft = 40;

    /**
     * Percentage when WARN-messages will be logged if an {@link AccessToken}
     * couldn't refreshed for any reason so far.
     */
    private int warnPercentLeft = 20;

    /**
     * Path to directory where credentials can be found (e.g. client.json,
     * user.json).
     * 
     * Defaults to : /meta/credentials
     */
    private String credentialsDirectory = getCredentialsDirectoryPath(CREDENTIALS_DIR, DEFAULT_CREDENTIALS_DIR);

    /**
     * Filename for user-credentials.
     * 
     * Defaults to : user.json
     */
    private String userCredentialsFilename = USER_JSON;

    /**
     * Filename for client-credentials.
     * 
     * Defaults to : client.json
     */
    private String clientCredentialsFilename = CLIENT_JSON;

    /**
     * To provide 'token' for test-cases.
     * 
     * Defaults to : null
     */
    private String testTokens = null;

    /**
     * Is 'auto-startup' enabled.
     * 
     * Defaults to : true
     * 
     * @see SmartLifecycle
     */
    private boolean autoStartup = true;

    /**
     * In which 'phase' should the bean started.
     * 
     * Defaults to : 0
     * 
     * @see SmartLifecycle
     */
    private int phase = 0;

    /**
     * If the {@link ClientCredentialsProvider} should be exposed as bean set
     * this property to 'true'.
     * 
     * Defaults to : false
     */
    private boolean exposeClientCredentialProvider = false;

    /**
     * If the {@link AccessTokensBean} should be started immediately after
     * creation, set this property to 'true'.
     * 
     * Defaults to : true
     */
    private boolean startAfterCreation = true;

    /**
     * If a mock of {@link AccessTokensBean} is needed set this property to
     * 'true'. Maybe for testing-cases.
     * 
     * Defaults to : false
     */
    private boolean enableMock = false;

    /**
     * List of {@link TokenConfiguration}s.
     */
    private List<TokenConfiguration> tokenConfigurationList = new ArrayList<TokenConfiguration>(0);

    /**
     * Provide an existing {@link SchedulingTaskExecutor} to use in the
     * refresher.
     */
    private boolean useExistingScheduler = true;

    /**
     * Configuration for Circuit-Breaker of refresher.
     */
    private CircuitBreakerConfiguration refresherCircuitBreaker = new CircuitBreakerConfiguration("MCB-Refresher");

    /**
     * Configuration for Circuit-Breaker of verifier.
     */
    private CircuitBreakerConfiguration verifierCircuitBreaker = new CircuitBreakerConfiguration(3, 10, 3,
            TimeUnit.MINUTES, "MCB-Verifier");

    /**
     * The url for the token-info-endpoint (e.g.
     * http://auth.example.com/oauth2/tokeninfo)
     * 
     * Defaults to : 'TOKENINFO_URL' environment variable
     */
    private URI tokenInfoUri;

    /**
     * Scheduling period for the refresher.
     * 
     * Defaults to : 5
     */
    private int refresherSchedulingPeriod = 5;

    /**
     * {@link TimeUnit} for the scheduling period of the refresher.
     * 
     * Defaults. to : {@link TimeUnit#SECONDS}
     */
    private TimeUnit refresherSchedulingTimeUnit = TimeUnit.SECONDS;

    /**
     * Scheduling period for the verifier.
     * 
     * Defaults to : 5
     */
    private int verifierSchedulingPeriod = 5;

    /**
     * {@link TimeUnit} for the scheduling period of the verifier.
     * 
     * Defaults to : {@link TimeUnit#MINUTES}
     */
    private TimeUnit verifierSchedulingTimeUnit = TimeUnit.MINUTES;

    private static String getFromEnvOrNull(String property) {
        String value = System.getenv(property);
        if (org.springframework.util.StringUtils.hasText(value)) {
            return value;
        } else {
            return null;
        }
    }

    private static String getFromEnvOrDefault(String property, String defaultValue) {
        String value = getFromEnvOrNull(property);
        if (org.springframework.util.StringUtils.hasText(value)) {
            return value;
        } else {
            return defaultValue;
        }
    }
    
    private static String getCredentialsDirectoryPath(String property, String defaultValue) {
        return getFromEnvOrDefault(property, defaultValue);
//        if (new File(value).exists()) {
//            return value;
//        } else {
//            throw new RuntimeException(String.format("The 'CREDENTIALS_DIR' -- %s-- does not exists.", value));
//        }
    }

    private static URI initializeAccessTokenUrlFromEnvironment() {
        String value = getFromEnvOrNull(OAUTH2_ACCESS_TOKEN_URL);
        if (value == null) {
            return null;
        } else if (StringUtils.hasText(value)) {
            try {
                return URI.create(value);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public URI getAccessTokenUri() {
        return accessTokenUri;
    }

    public void setAccessTokenUri(final URI accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    public int getRefreshPercentLeft() {
        return refreshPercentLeft;
    }

    public void setRefreshPercentLeft(final int refreshPercentLeft) {
        this.refreshPercentLeft = refreshPercentLeft;
    }

    public int getWarnPercentLeft() {
        return warnPercentLeft;
    }

    public void setWarnPercentLeft(final int warnPercentLeft) {
        this.warnPercentLeft = warnPercentLeft;
    }

    public String getCredentialsDirectory() {
        return credentialsDirectory;
    }

    public void setCredentialsDirectory(final String credentialsDirectory) {
        this.credentialsDirectory = credentialsDirectory;
    }

    public List<TokenConfiguration> getTokenConfigurationList() {
        return tokenConfigurationList;
    }

    public String getUserCredentialsFilename() {
        return userCredentialsFilename;
    }

    public String getClientCredentialsFilename() {
        return clientCredentialsFilename;
    }

    public String getTestTokens() {
        return testTokens;
    }

    public void setTestTokens(final String testTokens) {
        this.testTokens = testTokens;
    }

    public boolean isAutoStartup() {
        return autoStartup;
    }

    public void setAutoStartup(final boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(final int phase) {
        this.phase = phase;
    }

    public boolean isExposeClientCredentialProvider() {
        return exposeClientCredentialProvider;
    }

    public void setExposeClientCredentialProvider(boolean exposeClientCredentialProvider) {
        this.exposeClientCredentialProvider = exposeClientCredentialProvider;
    }

    public boolean isStartAfterCreation() {
        return startAfterCreation;
    }

    public void setStartAfterCreation(boolean startAfterCreation) {
        this.startAfterCreation = startAfterCreation;
    }

    public boolean isEnableMock() {
        return enableMock;
    }

    public void setEnableMock(boolean enableMock) {
        this.enableMock = enableMock;
    }

    public boolean isUseExistingScheduler() {
        return useExistingScheduler;
    }

    public void setUseExistingScheduler(boolean useExistingScheduler) {
        this.useExistingScheduler = useExistingScheduler;
    }

    public CircuitBreakerConfiguration getRefresherCircuitBreaker() {
        return refresherCircuitBreaker;
    }

    public void setRefresherCircuitBreaker(CircuitBreakerConfiguration refresherCircuitBreaker) {
        this.refresherCircuitBreaker = refresherCircuitBreaker;
    }

    public CircuitBreakerConfiguration getVerifierCircuitBreaker() {
        return verifierCircuitBreaker;
    }

    public void setVerifierCircuitBreaker(CircuitBreakerConfiguration verifierCircuitBreaker) {
        this.verifierCircuitBreaker = verifierCircuitBreaker;
    }

    public URI getTokenInfoUri() {
        return tokenInfoUri;
    }

    public void setTokenInfoUri(URI tokenInfoUri) {
        this.tokenInfoUri = tokenInfoUri;
    }

    public int getVerifierSchedulingPeriod() {
        return verifierSchedulingPeriod;
    }

    public void setVerifierSchedulingPeriod(int tokenVerifierSchedulingPeriod) {
        this.verifierSchedulingPeriod = tokenVerifierSchedulingPeriod;
    }

    public int getRefresherSchedulingPeriod() {
        return refresherSchedulingPeriod;
    }

    public void setRefresherSchedulingPeriod(int schedulingPeriod) {
        this.refresherSchedulingPeriod = schedulingPeriod;
    }

    public TimeUnit getRefresherSchedulingTimeUnit() {
        return refresherSchedulingTimeUnit;
    }

    public void setRefresherSchedulingTimeUnit(TimeUnit schedulingTimeUnit) {
        this.refresherSchedulingTimeUnit = schedulingTimeUnit;
    }

    public TimeUnit getVerifierSchedulingTimeUnit() {
        return verifierSchedulingTimeUnit;
    }

    public void setVerifierSchedulingTimeUnit(TimeUnit tokenVerifierSchedulingTimeUnit) {
        this.verifierSchedulingTimeUnit = tokenVerifierSchedulingTimeUnit;
    }

}
