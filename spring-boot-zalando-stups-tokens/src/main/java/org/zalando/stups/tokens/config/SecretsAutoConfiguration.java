package org.zalando.stups.tokens.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.stups.tokens.AccessTokensBean;
import org.zalando.stups.tokens.Secrets;

/**
 * 
 * @author jbellmann
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "k8s", name = "enabled", havingValue = "true")
public class SecretsAutoConfiguration {

    @Bean
    public Secrets secrets(AccessTokensBean accessTokensBean) {
        return (Secrets) accessTokensBean.getDelegate();
    }

}
