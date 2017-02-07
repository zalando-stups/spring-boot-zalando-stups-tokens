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
