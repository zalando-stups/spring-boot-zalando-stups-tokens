package org.zalando.spring.boot.k8s;

import org.springframework.context.annotation.Conditional;

@Conditional(OnKubernetesNodeCondition.class)
public @interface ConditionalOnKubernetesNode {
}
