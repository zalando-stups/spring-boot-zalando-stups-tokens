package org.zalando.stups.examples.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.zalando.stups.tokens.annotation.OAuth2RestOperations;
import org.zalando.stups.tokens.annotation.OAuth2RestOperationsAutowired;

@Configuration
public class RestConfig {

    private static final String PRODUCER_TOKEN_NAME = "producer";

    @Value("${PRODUCER_EVENTS_URI:http://noip.test.de}")
    private String producerEventsURI;

    @OAuth2RestOperationsAutowired(PRODUCER_TOKEN_NAME)
    private RestOperations template;

    @Bean
    public UseProducerRestOperation userProducerRestOperation(
            @OAuth2RestOperations(PRODUCER_TOKEN_NAME) RestOperations template) {
        return new UseProducerRestOperation(template);
    }
}
