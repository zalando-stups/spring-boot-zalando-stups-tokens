package org.zalando.stups.tokens.config;

import static java.util.Objects.requireNonNull;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.stups.tokens.AccessTokensBean;
import org.zalando.stups.tokens.ClientCredentials;
import org.zalando.stups.tokens.Secret;
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
        return new SecretsAdapter((Secrets) accessTokensBean.getDelegate());
    }

    static class SecretsAdapter implements Secrets {

        private Secrets delegate;

        SecretsAdapter(Secrets delegate) {
            this.delegate = requireNonNull(delegate, "'secrets'-delegate should never be null");
        }

        @Override
        public Secret getSecret(String identifier) {
            return delegate.getSecret(identifier);
        }

        @Override
        public ClientCredentials getClient(String identifier) {
            return delegate.getClient(identifier);
        }

    }

}
