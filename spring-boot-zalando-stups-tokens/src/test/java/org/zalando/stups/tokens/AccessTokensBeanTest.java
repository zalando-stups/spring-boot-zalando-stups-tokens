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

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.zalando.stups.tokens.config.AccessTokensBeanProperties;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.zalando.stups.tokens.AccessTokensBean.OAUTH2_ACCESS_TOKENS;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AccessTokensBean.class, AccessTokensBeanTest.class })
public class AccessTokensBeanTest {

    @After
    public void tearDown() {
        System.clearProperty(OAUTH2_ACCESS_TOKENS);
    }

    @Test
    public void testLocalTesting() {
        AccessTokensBeanProperties properties = new AccessTokensBeanProperties();

        AccessTokensBean bean = new AccessTokensBean(properties);
        assertThat(bean.isTestingConfigured()).isFalse();
    }

    @Test
    public void testLocalTestingWhenYamlConfigured() {
        AccessTokensBeanProperties properties = new AccessTokensBeanProperties();
        properties.setTestTokens("ANY_TEXT");

        AccessTokensBean bean = new AccessTokensBean(properties);
        assertThat(bean.isTestingConfigured()).isTrue();
    }

    @Test
    public void testLocalTestingWhenEnvConfigured() {
        PowerMockito.mockStatic(System.class);
        when(System.getenv(OAUTH2_ACCESS_TOKENS)).thenReturn("ANY_TEXT");
        when(System.getProperties()).thenReturn(new Properties());

        AccessTokensBeanProperties properties = new AccessTokensBeanProperties();

        AccessTokensBean bean = new AccessTokensBean(properties);

        assertThat(bean.isTestingConfigured()).isTrue();
    }

    @Test
    public void testLocalTestingWithEnvAndYamlConfig() {
        System.setProperty(OAUTH2_ACCESS_TOKENS, "ANY_TEXT");

        AccessTokensBeanProperties properties = new AccessTokensBeanProperties();
        properties.setTestTokens("ANY_TEXT");

        AccessTokensBean bean = new AccessTokensBean(properties);
        assertThat(bean.isTestingConfigured()).isTrue();
    }

}
