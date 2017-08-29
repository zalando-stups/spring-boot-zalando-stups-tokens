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
package com.unknown.pkg;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zalando.stups.tokens.ClientCredentialsProvider;

/**
 * @author  jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TokenTestApplication.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("notExposeClient")
public class DoNotClientCredentialsProviderIT {

    static final String OAUTH2_ACCESS_TOKENS = "OAUTH2_ACCESS_TOKENS";
    
    @Autowired(required=false)
    private ClientCredentialsProvider clientCredentialsProvider;

    @BeforeClass
    public static void setUp() {
        System.getProperties().remove(OAUTH2_ACCESS_TOKENS);
    }

    @Test
    public void testClientCredentialProviderIsPresent() throws InterruptedException {
    	Assertions.assertThat(clientCredentialsProvider).isNull();
    }
}
