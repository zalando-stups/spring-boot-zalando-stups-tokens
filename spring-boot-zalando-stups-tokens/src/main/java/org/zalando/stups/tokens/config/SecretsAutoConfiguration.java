package org.zalando.stups.tokens.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.spring.boot.k8s.ConditionalOnKubernetesNode;
import org.zalando.stups.tokens.AccessTokensBean;
import org.zalando.stups.tokens.Secrets;

/**
 * 
 * @author jbellmann
 *
 */
@Configuration
@ConditionalOnKubernetesNode
public class SecretsAutoConfiguration {

    @Bean
    public Secrets secrets(AccessTokensBean accessTokensBean) {
        return (Secrets) accessTokensBean.getDelegate();
    }

}
