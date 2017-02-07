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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.zalando.secrets.Authorizations;
import org.zalando.secrets.Clients;
import org.zalando.stups.tokens.AccessTokens;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("disabled")
public class SecretsDisabledTest {

    @Autowired(required=false)
    private AccessTokens accessTokens;

    @Autowired(required=false)
    private Authorizations authorizations;

    @Autowired(required=false)
    private Clients clients;

    @Test
    public void contextLoads() throws InterruptedException {
        assertThat(accessTokens).isNull();
        assertThat(authorizations).isNull();
        assertThat(clients).isNull();
    }
}
