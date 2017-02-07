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

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.zalando.secrets.Authorization;
import org.zalando.secrets.Authorizations;
import org.zalando.secrets.ClientCredentials;
import org.zalando.secrets.Clients;
import org.zalando.stups.tokens.AccessToken;
import org.zalando.stups.tokens.AccessTokens;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleApplicationTest {

    @Autowired
    private AccessTokens accessTokens;

    @Autowired
    private Authorizations authorizations;

    @Autowired
    private Clients clients;

    @Test
    public void contextLoads() throws InterruptedException {
        AccessToken at = accessTokens.getAccessToken("full-access");
        Assertions.assertThat(at).isNotNull();
        Authorization auth = authorizations.get("read-only");
        Assertions.assertThat(auth).isNotNull();

        Optional<ClientCredentials> cc = clients.get("employee");
        Assertions.assertThat(cc.get()).isNotNull();
    }
}
