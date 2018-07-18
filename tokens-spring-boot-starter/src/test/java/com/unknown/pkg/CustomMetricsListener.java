package com.unknown.pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.zalando.stups.tokens.AccessTokensBean;
import org.zalando.stups.tokens.MetricsListener;
import org.zalando.stups.tokens.config.AccessTokensBeanAutoConfiguration;

/**
 * Will be picked up by spring and then provided to {@link AccessTokensBean} per
 * {@link AccessTokensBeanAutoConfiguration}.
 * 
 * @author jbellmann
 *
 */
@Component
public class CustomMetricsListener implements MetricsListener {

    private final Logger log = LoggerFactory.getLogger(CustomMetricsListener.class);

    @Override
    @Async // optional, but recommended
    public void submitToTimer(String key, long time) {
        log.warn("CUSTOM_METRICSLISTENER : {} needs {} ms", key, time);
    }

}
