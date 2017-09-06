package org.zalando.spring.boot.k8s.actuator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class K8sInfoContributor implements InfoContributor {

    private final Environment environment;

    @Override
    public void contribute(Builder builder) {
        Map<String, Object> props = new HashMap<>();
        props.put("namespace", environment.getProperty("k8s.namespace", "UNKNOWN"));
        props.put("pod", environment.getProperty("HOSTNAME", "UNKNOWN"));
        builder.withDetail("k8s", props);
    }

}
