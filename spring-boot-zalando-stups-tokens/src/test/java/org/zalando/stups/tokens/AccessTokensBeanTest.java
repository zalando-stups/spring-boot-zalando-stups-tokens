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
