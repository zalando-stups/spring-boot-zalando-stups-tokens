package org.zalando.stups.examples;

import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.web.client.RestOperations;
import org.zalando.stups.examples.config.UseProducerRestOperation;

@SpringApplicationConfiguration(classes = ExampleApp.class)
@WebIntegrationTest
public class RunApplicationTest {

    @Rule
    public SpringMethodRule methodRule = new SpringMethodRule();

    @ClassRule
    public static final SpringClassRule clazzRule = new SpringClassRule();

    @Autowired
    private UseProducerRestOperation useProducerRestOperation;

    @Test
    public void run() throws InterruptedException {
        RestOperations restOperations = useProducerRestOperation.getRestOperations();
        Assertions.assertThat(restOperations).isNotNull();
        TimeUnit.SECONDS.sleep(5);
    }

}
