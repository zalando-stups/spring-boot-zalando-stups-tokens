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
import org.zalando.secrets.ClientCredentials;
import org.zalando.secrets.Clients;

public class ClientsBeanTest {

    @Test
    public void testClientsBean() throws InterruptedException {
        SecretsProperties props = new SecretsProperties();
        props.setCredentialsDirectory(
                "/Users/jbellmann/dev/work/zalando/ghcom/spring-boot-zalando-stups-tokens/tokens-k8s-spring-boot-starter/credentials");
        ClientsBean cb = new ClientsBean(props);
        cb.initialize();
        Optional<ClientCredentials> clientOptional = ((Clients) cb).get("employee");
        Assertions.assertThat(clientOptional.isPresent()).isTrue();
    }

}
