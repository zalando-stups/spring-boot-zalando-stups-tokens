package org.zalando.spring.boot.k8s;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ContributorApplicationTest {

    @LocalServerPort
    private int port;

    @Test
    public void contextLoads() {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> o = rest.getForEntity("http://localhost:" + port + "/info", String.class);
        assertThat(o.getBody()).doesNotContain("k8s");
    }
}
