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

/**
 * @author jbellmann
 */
@ConfigurationProperties(prefix = "tokens")
public class AccessTokensBeanProperties {

    private static final String CLIENT_JSON = "client.json";

    private static final String USER_JSON = "user.json";

    private URI accessTokenUri;

    private int refreshPercentLeft = 40;

    private int warnPercentLeft = 20;

    private String credentialsDirectory;

    private String userCredentialsFilename = USER_JSON;

    private String clientCredentialsFilename = CLIENT_JSON;

    private String testTokens = null;

    private boolean autoStartup = true;

    private int phase = 0;

    private boolean exposeClientCredentialProvider = false;

    private boolean startAfterCreation = false;

    private boolean enableMock = false;

    private List<TokenConfiguration> tokenConfigurationList = new ArrayList<TokenConfiguration>(0);

    private boolean useExistingScheduler = true;

    private CircuitBreakerConfiguration tokenRefresherCircuitBreaker = new CircuitBreakerConfiguration();

    private CircuitBreakerConfiguration tokenVerifierCircuitBreaker = new CircuitBreakerConfiguration(3, 10, 3,
            TimeUnit.MINUTES);

    private URI tokenInfoUri;

    private int schedulingPeriod = 5;

    private TimeUnit schedulingTimeUnit = TimeUnit.SECONDS;

    private int tokenVerifierSchedulingPeriod = 5;

    private TimeUnit tokenVerifierSchedulingTimeUnit = TimeUnit.MINUTES;

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

    public CircuitBreakerConfiguration getTokenRefresherCircuitBreaker() {
        return tokenRefresherCircuitBreaker;
    }

    public void setTokenRefresherCircuitBreaker(CircuitBreakerConfiguration tokenRefresherCircuitBreaker) {
        this.tokenRefresherCircuitBreaker = tokenRefresherCircuitBreaker;
    }

    public CircuitBreakerConfiguration getTokenVerifierCircuitBreaker() {
        return tokenVerifierCircuitBreaker;
    }

    public void setTokenVerifierCircuitBreaker(CircuitBreakerConfiguration tokenVerifierCircuitBreaker) {
        this.tokenVerifierCircuitBreaker = tokenVerifierCircuitBreaker;
    }

    public URI getTokenInfoUri() {
        return tokenInfoUri;
    }

    public void setTokenInfoUri(URI tokenInfoUri) {
        this.tokenInfoUri = tokenInfoUri;
    }

    public int getTokenVerifierSchedulingPeriod() {
        return tokenVerifierSchedulingPeriod;
    }

    public void setTokenVerifierSchedulingPeriod(int tokenVerifierSchedulingPeriod) {
        this.tokenVerifierSchedulingPeriod = tokenVerifierSchedulingPeriod;
    }

    public int getSchedulingPeriod() {
        return schedulingPeriod;
    }

    public void setSchedulingPeriod(int schedulingPeriod) {
        this.schedulingPeriod = schedulingPeriod;
    }

    public TimeUnit getSchedulingTimeUnit() {
        return schedulingTimeUnit;
    }

    public void setSchedulingTimeUnit(TimeUnit schedulingTimeUnit) {
        this.schedulingTimeUnit = schedulingTimeUnit;
    }

    public TimeUnit getTokenVerifierSchedulingTimeUnit() {
        return tokenVerifierSchedulingTimeUnit;
    }

    public void setTokenVerifierSchedulingTimeUnit(TimeUnit tokenVerifierSchedulingTimeUnit) {
        this.tokenVerifierSchedulingTimeUnit = tokenVerifierSchedulingTimeUnit;
    }

}
