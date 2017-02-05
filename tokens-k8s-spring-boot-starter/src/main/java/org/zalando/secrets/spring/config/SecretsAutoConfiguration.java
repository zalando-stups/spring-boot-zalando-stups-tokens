/**
 * Copyright (C) 2017 Zalando SE (http://tech.zalando.com)
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
package org.zalando.secrets.spring.config;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.isTrue;

import java.io.File;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.zalando.secrets.spring.AccessTokensBean;
import org.zalando.secrets.spring.ClientsBean;
import org.zalando.secrets.spring.SecretsProperties;

@Configuration
@EnableConfigurationProperties({ SecretsProperties.class })
@EnableScheduling
public class SecretsAutoConfiguration {

    @Bean
    public AccessTokensBean accessTokens(SecretsProperties secretsProperties) {
        credentialsDirectoryMustExists(secretsProperties);
        return new AccessTokensBean(secretsProperties);
    }

    @Bean
    public ClientsBean clients(SecretsProperties secretsProperties) {
        credentialsDirectoryMustExists(secretsProperties);
        return new ClientsBean(secretsProperties);
    }

    protected void credentialsDirectoryMustExists(SecretsProperties secretsProperties) {
        String credentialsDirectoryPath = requireNonNull(secretsProperties.getCredentialsDirectory());
        isTrue(new File(credentialsDirectoryPath).exists(), "Credentials-Directory does not exists");
    }
}
