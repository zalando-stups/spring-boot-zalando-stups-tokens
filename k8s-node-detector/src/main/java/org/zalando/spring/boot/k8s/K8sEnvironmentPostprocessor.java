package org.zalando.spring.boot.k8s;

import java.io.File;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * 
 * @author jbellmann
 *
 */
public class K8sEnvironmentPostprocessor implements EnvironmentPostProcessor, Ordered {

    private final Logger log = LoggerFactory.getLogger(K8sEnvironmentPostprocessor.class);

    public static final String K8S_PREFIX = "k8s";
    public static final String K8S_ENABLED_KEY = K8S_PREFIX + ".enabled";

    private final String namespacePath = "/var/run/secrets/kubernetes.io/serviceaccount/namespace";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        log.info("Detecting 'K8S'-Environment ...");
        Properties properties = new Properties();
        if (specificFilesystemLayoutExists()) {
            properties.put(K8S_ENABLED_KEY, Boolean.TRUE.toString());
            log.info("'K8S'-metadata : {}", properties.toString());
        } else {
            log.info("Ignore 'K8S', no metadata available.");
            properties.put(K8S_ENABLED_KEY, Boolean.FALSE.toString());
        }

        //
        MutablePropertySources propertySources = environment.getPropertySources();
        if (propertySources.contains(CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME)) {
            propertySources.addAfter(CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME,
                    new PropertiesPropertySource(K8S_PREFIX, properties));
        } else {
            propertySources.addFirst(new PropertiesPropertySource(K8S_PREFIX, properties));
        }

    }

    protected boolean specificFilesystemLayoutExists() {
        return new File(namespacePath).exists();
    }

    // Before ConfigFileApplicationListener, so values there can use these ones
    private int order = ConfigFileApplicationListener.DEFAULT_ORDER - 1;

    @Override
    public int getOrder() {
        return order;
    }

}
