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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
	public AccessTokensBean accessTokensBean() {
		if(accessTokensBeanProperties.isEnableMock()){
			return new MockAccessTokensBean(accessTokensBeanProperties);
		}

		//
		AccessTokensBean bean = new AccessTokensBean(accessTokensBeanProperties);
        bean.setMetricsListeners(metricsListeners);
		if (accessTokensBeanProperties.isStartAfterCreation()) {
			logger.info("'accessTokensBean' was configured to 'startAfterCreation', starting now ...");
			bean.start();
		}
		return bean;
	}

	@Bean
	@ConditionalOnProperty(prefix = "tokens", name = "exposeClientCredentialProvider", havingValue = "true")
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
		private static final String INVALID = "INVALID";
		private final Date validUnti = new Date();

		public MockAccessTokensBean(AccessTokensBeanProperties accessTokensBeanProperties) {
			super(accessTokensBeanProperties);
		}

		@Override
		public synchronized void start() {
			logger.warn("USING MOCK_ACCESS_TOKENS_BEAN, OAUTH WILL NOT WORK !!!");
			accessTokensDelegate = new AccessTokens() {
				
				@Override
				public void stop() {
				}
				
				@Override
				public void invalidate(Object tokenId) {
					
				}
				
				@Override
				public AccessToken getAccessToken(Object tokenId) throws AccessTokenUnavailableException {
					return new AccessToken(INVALID, BEARER, -1, validUnti);
				}
				
				@Override
				public String get(Object tokenId) throws AccessTokenUnavailableException {
					return INVALID;
				}
			};
		}
		
		
		
	}

}
