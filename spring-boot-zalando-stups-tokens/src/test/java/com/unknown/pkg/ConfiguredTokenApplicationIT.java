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
package com.unknown.pkg;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zalando.stups.tokens.AccessToken;
import org.zalando.stups.tokens.AccessTokens;
import org.zalando.stups.tokens.AccessTokensBean;
import org.zalando.stups.tokens.config.AccessTokensBeanProperties;
import org.zalando.stups.tokens.config.TokenConfiguration;

import com.google.common.base.Predicate;

/**
 * @author  jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TokenTestApplication.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("testTokens")
public class ConfiguredTokenApplicationIT {

    static final String OAUTH2_ACCESS_TOKENS = "OAUTH2_ACCESS_TOKENS";

    @Autowired
    private AccessTokensBean tokens;

    @Autowired
    private AccessTokensBeanProperties accessTokensBeanProperties;

    @Autowired
    private AccessTokens accessTokens;

    @BeforeClass
    public static void setUp() {
        System.getProperties().remove(OAUTH2_ACCESS_TOKENS);
    }

    @Test
    public void retrieveToken() throws InterruptedException {

        // give the controller a chance to initialize
        TimeUnit.SECONDS.sleep(2);

        Assertions.assertThat(tokens.isRunning()).isTrue();

        // calling start after it is started should not cause any errors
        tokens.start();

        // it should be running
        Assertions.assertThat(tokens.isRunning()).isTrue();
        testing();

        accessTokens.invalidate("firstService");

        tokens.stop(new Runnable() {

                @Override
                public void run() {
                    System.out.println("STOPPED");
                }
            });

        //
        tokens.stop();

        Assertions.assertThat(tokens.isRunning()).isFalse();

        tokens.start();

        Assertions.assertThat(tokens.isRunning()).isTrue();
        TimeUnit.SECONDS.sleep(2);
        testing();

        System.out.println("KILL");
    }

    protected void testing() {
        Assertions.assertThat(accessTokensBeanProperties).isNotNull();
        Assertions.assertThat(accessTokensBeanProperties.getTokenConfigurationList()).isNotEmpty();

        Assertions.assertThat(accessTokens).isNotNull();

        List<TokenConfiguration> services = accessTokensBeanProperties.getTokenConfigurationList();
        Assertions.assertThat(services.size()).isGreaterThan(0);

        AccessToken accessToken = accessTokens.getAccessToken("firstService");
        Assertions.assertThat(accessToken).isNotNull();
        Assertions.assertThat(accessToken.getToken()).isEqualTo("12345");
        Assertions.assertThat(accessToken.getType()).isEqualTo("fixed");
        Assertions.assertThat(accessToken.getInitialValidSeconds()).isEqualTo(31536000);

        String accessTokenString = accessTokens.get("secondService");
        Assertions.assertThat(accessTokenString).isEqualTo("56789");
    }

    static class TokenIdFilter implements Predicate<TokenConfiguration> {

        private final String tokenId;

        public TokenIdFilter(final String tokenId) {
            this.tokenId = tokenId;
        }

        @Override
        public boolean apply(final TokenConfiguration input) {
            return input.getTokenId().equals(tokenId);
        }

    }
}
