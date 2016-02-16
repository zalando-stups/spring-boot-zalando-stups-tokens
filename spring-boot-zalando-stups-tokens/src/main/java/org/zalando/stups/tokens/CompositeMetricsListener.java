package org.zalando.stups.tokens;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

class CompositeMetricsListener implements MetricsListener {

    private final Logger log = LoggerFactory.getLogger(CompositeMetricsListener.class);
    private final List<MetricsListener> metricsListeners;

    CompositeMetricsListener(List<MetricsListener> metricsListeners) {
        Assert.notNull(metricsListeners, "metricsListeners should never be null");
        this.metricsListeners = metricsListeners;
    }

    @Override
    public void submitToTimer(String key, long time) {
        for (MetricsListener l : metricsListeners) {
            try {
                l.submitToTimer(key, time);
            } catch (Throwable t) {
                log.warn(t.getMessage(), t);
            }
        }

    }

}
