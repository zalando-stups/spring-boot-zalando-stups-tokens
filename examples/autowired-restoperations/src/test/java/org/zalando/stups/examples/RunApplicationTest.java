package org.zalando.stups.examples;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.zalando.stups.examples.config.UseProducerRestOperation;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

@SpringApplicationConfiguration(classes = ExampleApp.class)
@WebIntegrationTest()
public class RunApplicationTest {

    @Rule
    public SpringMethodRule methodRule = new SpringMethodRule();

    @ClassRule
    public static final SpringClassRule clazzRule = new SpringClassRule();

    @ClassRule
    public static final WireMockRule wireMockRule = new WireMockRule(9998);

    @Autowired
    private UseProducerRestOperation useProducerRestOperation;

    @Value("${local.server.port}")
    private int port;

    @Before
    public void setUp() {
        wireMockRule.stubFor(get(urlPathEqualTo("/listenable"))
                .willReturn(aResponse().withStatus(200).withBody("{\"key\":\"value\"}")
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
    }

    @Test
    public void run() throws InterruptedException {
        RestOperations restOperations = useProducerRestOperation.getRestOperations();
        Assertions.assertThat(restOperations).isNotNull();

        RestTemplate wireMockRestTemplate = new RestTemplate();
        ResponseEntity<String> wireMockResponse = wireMockRestTemplate.getForEntity("http://localhost:9998/listenable",
                String.class);
        System.out.println("WIREMOCK_SAYS : " + wireMockResponse.getBody());

        //
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/test",
                String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("BODDYYY ::: " + response.getBody());

        RestTemplate restTemplate2 = new RestTemplate();
        ResponseEntity<String> response2 = restTemplate2.getForEntity("http://localhost:" + port + "/secondTest",
                String.class);
        Assertions.assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("BODDYYY-2 ::: " + response2.getBody());

        RestTemplate restTemplate3 = new RestTemplate();
        ResponseEntity<String> response3 = restTemplate3.getForEntity("http://localhost:" + port + "/thirdTest",
                String.class);
        Assertions.assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("BODDYYY-3 ::: " + response3.getBody());

        TimeUnit.SECONDS.sleep(5);
    }

}
