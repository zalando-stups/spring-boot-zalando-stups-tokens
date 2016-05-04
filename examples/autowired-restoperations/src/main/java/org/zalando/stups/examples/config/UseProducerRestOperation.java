package org.zalando.stups.examples.config;

import org.springframework.web.client.RestOperations;

/**
 * 
 * @author jbellmann
 *
 */
public class UseProducerRestOperation {

    private final RestOperations restOperations;

    public UseProducerRestOperation(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    public RestOperations getRestOperations() {
        return restOperations;
    }

}
