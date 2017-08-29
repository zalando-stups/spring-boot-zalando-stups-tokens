package org.zalando.spring.boot.k8s;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

class OnKubernetesNodeCondition implements ConfigurationCondition {

    private static final String K8S_ENABLED = "k8s.enabled";

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment().getProperty(K8S_ENABLED, Boolean.class, false);
    }

}
