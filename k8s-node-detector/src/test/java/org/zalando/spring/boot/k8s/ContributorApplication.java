package org.zalando.spring.boot.k8s;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContributorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContributorApplication.class, args);
    }
}
