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

import static org.zalando.stups.tokens.AccessTokensBean.OAUTH2_ACCESS_TOKENS;

import org.assertj.core.api.Assertions;

import org.junit.After;
import org.junit.Test;

import org.zalando.stups.tokens.config.AccessTokensBeanProperties;

public class AccessTokensBeanTest {

    @After
    public void tearDown() {
        System.getProperties().remove(OAUTH2_ACCESS_TOKENS);
    }

    @Test
    public void testLocalTesting() {
        AccessTokensBeanProperties properties = new AccessTokensBeanProperties();

        AccessTokensBean bean = new AccessTokensBean(properties);
        Assertions.assertThat(bean.isTestingConfigured()).isFalse();
    }

    @Test
    public void testLocalTestingWhenYamlConfigured() {
        AccessTokensBeanProperties properties = new AccessTokensBeanProperties();
        properties.setTestTokens("ANY_TEXT");

        AccessTokensBean bean = new AccessTokensBean(properties);
        Assertions.assertThat(bean.isTestingConfigured()).isTrue();
    }

    @Test
    public void testLocalTestingWhenEnvConfigured() {
        System.setProperty(OAUTH2_ACCESS_TOKENS, "ANY_TEXT");

        AccessTokensBeanProperties properties = new AccessTokensBeanProperties();

        AccessTokensBean bean = new AccessTokensBean(properties);

        Assertions.assertThat(bean.isTestingConfigured()).isTrue();
    }

    @Test
    public void testLocalTestingWithEnvAndYamlConfig() {
        System.setProperty(OAUTH2_ACCESS_TOKENS, "ANY_TEXT");

        AccessTokensBeanProperties properties = new AccessTokensBeanProperties();
        properties.setTestTokens("ANY_TEXT");

        AccessTokensBean bean = new AccessTokensBean(properties);
        Assertions.assertThat(bean.isTestingConfigured()).isTrue();
    }

}
