package org.zalando.spring.boot.k8s.actuator;

import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.zalando.spring.boot.k8s.ConditionalOnKubernetesNode;

@ConditionalOnClass({ InfoContributor.class })
@ConditionalOnKubernetesNode
@Configuration
class InfoContributorConfig {

    @Bean
    K8sInfoContributor k8sInfoContributor(Environment environment) {
        return new K8sInfoContributor(environment);
    }
}
