package org.zalando.stups.examples;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * Just to show that ResponseEntity<String> will not result in escaped json.
 * 
 * @author jbellmann
 *
 */
@RestController
public class Controller {

    private final Logger log = LoggerFactory.getLogger(Controller.class);

    private AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate(
            new HttpComponentsAsyncClientHttpRequestFactory());

    private final ThreadPoolTaskExecutor tpte;

    public Controller() {
        tpte = new ThreadPoolTaskExecutor();
        tpte.afterPropertiesSet();
    }

    @RequestMapping("/test")
    public ResponseEntity<String> get() throws IOException {
        log.warn("CONTROLLER INVOKED ... ");

        RestTemplate wireMockRestTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<String> wireMockResponse = wireMockRestTemplate.getForEntity("http://localhost:9998/listenable",
                String.class);

        return ResponseEntity.ok().body(wireMockResponse.getBody());
    }

    @RequestMapping("/secondTest")
    public ListenableFuture<ResponseEntity<String>> getSecond() throws IOException {
        log.warn("CONTROLLER SECOND_TEST INVOKED ... ");
        return this.tpte.submitListenable(new Callable<ResponseEntity<String>>() {
            @Override
            public ResponseEntity<String> call() throws Exception {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"key\":\"value\"}");
            }
        });
    }

    @RequestMapping("/thirdTest")
    public ListenableFuture<ResponseEntity<String>> getThird() throws IOException {
        log.warn("CONTROLLER THIRD_TEST INVOKED ... ");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONNECTION, "close"); // to avoid Premature end
                                                      // of file
        HttpEntity<?> entity = new HttpEntity<>(headers);

        return asyncRestTemplate.exchange(URI.create("http://localhost:9998/listenable"), HttpMethod.GET, entity,
                String.class);
    }

    @RequestMapping("/fourthTest")
    public void getFourth(Writer writer) throws IOException {
        log.warn("CONTROLLER FOURTH_TEST INVOKED ... ");

        RestTemplate wireMockRestTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<String> wireMockResponse = wireMockRestTemplate.getForEntity("http://localhost:9998/listenable",
                String.class);

        writer.write(wireMockResponse.getBody());
    }
}
