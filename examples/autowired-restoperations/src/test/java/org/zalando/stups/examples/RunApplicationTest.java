package org.zalando.stups.examples;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.zalando.stups.examples.config.UseProducerRestOperation;

import com.fasterxml.jackson.databind.JsonNode;
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
    public void setUp() throws IOException {
        wireMockRule.stubFor(get(urlPathEqualTo("/listenable"))
                .willReturn(aResponse().withStatus(200).withBody("{\"key\":\"value\"}")
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        wireMockRule.stubFor(get(urlPathEqualTo("/listenableNotJson"))
                .willReturn(aResponse().withStatus(200).withBody(resourceToString(jsonResource("/notJson")))
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
    }

    protected Resource jsonResource(String filename) {
        return new ClassPathResource(filename + ".json", getClass());
    }

    public static String resourceToString(Resource resource) throws IOException {
        return StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
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
        ResponseEntity<JsonNode> response3 = restTemplate3.getForEntity("http://localhost:" + port + "/thirdTest",
                JsonNode.class);
        Assertions.assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("BODDYYY-3 ::: " + response3.getBody());

        RestTemplate restTemplate4 = new RestTemplate();
        ResponseEntity<String> response4 = restTemplate4.getForEntity("http://localhost:" + port + "/fourthTest",
                String.class);
        Assertions.assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("BODDYYY-4 ::: " + response4.getBody());

        HttpClient client = HttpClientBuilder.create().build();
        try {
            HttpResponse response5 = client
                    .execute(RequestBuilder.get("http://localhost:" + port + "/thirdTest").build());
            System.out.println("BODY-5" + EntityUtils.toString(response5.getEntity()));
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            HttpResponse response6 = client
                    .execute(RequestBuilder.get("http://localhost:" + port + "/fifthTest").build());
            System.out.println("BODY-6" + EntityUtils.toString(response6.getEntity()));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TimeUnit.SECONDS.sleep(5);
    }

}
