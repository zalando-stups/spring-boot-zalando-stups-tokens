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
package org.zalando.secrets.spring;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.zalando.secrets.Authorization;
import org.zalando.secrets.Authorizations;
import org.zalando.stups.tokens.AccessTokens;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccessTokensBeanTest {

    @Test
    public void readAccessTokens() {
        SecretsProperties props = new SecretsProperties();
        props.setCredentialsDirectory(
                "/Users/jbellmann/dev/work/zalando/ghcom/spring-boot-zalando-stups-tokens/tokens-k8s-spring-boot-starter/credentials");
        AccessTokensBean ab = new AccessTokensBean(props);
        ab.initialize();
        String fullAccessToken = ((AccessTokens) ab).get("full-access");
        Assertions.assertThat(fullAccessToken).isNotEmpty();
        String fullAccessTokenType = ((AccessTokens) ab).getAccessToken("full-access").getType();
        Assertions.assertThat(fullAccessTokenType).isEqualTo("Bearer");
    }

    @Test
    public void readAuthorizations() {
        SecretsProperties props = new SecretsProperties();
        props.setCredentialsDirectory(
                "/Users/jbellmann/dev/work/zalando/ghcom/spring-boot-zalando-stups-tokens/tokens-k8s-spring-boot-starter/credentials");
        AccessTokensBean ab = new AccessTokensBean(props);
        ab.initialize();

        String fullAccessTokenSecret = ((Authorizations) ab).get("full-access").getSecret();
        Assertions.assertThat(fullAccessTokenSecret).isNotEmpty();
        String fullAccessTokenType = ((Authorizations) ab).get("full-access").getType();
        Assertions.assertThat(fullAccessTokenType).isEqualTo("Bearer");
        Authorization auth = ((Authorizations) ab).get("full-access");
        log.info("Authorization: {}", auth.getHeaderValue());
    }
}
