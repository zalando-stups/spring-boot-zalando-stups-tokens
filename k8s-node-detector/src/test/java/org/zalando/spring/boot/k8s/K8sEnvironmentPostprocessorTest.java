package org.zalando.spring.boot.k8s;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;

public class K8sEnvironmentPostprocessorTest {

    @Test
    public void testFileLayoutDetection() {
        assertThat(new File("/nonexistent/file.txt").exists()).isFalse();
    }

    @Test
    public void testCreateEnvironmentPostprocessor() {
        K8sEnvironmentPostprocessor postprocessor = new K8sEnvironmentPostprocessor();
        assertThat(postprocessor.specificFilesystemLayoutExists()).isFalse();
    }
}
