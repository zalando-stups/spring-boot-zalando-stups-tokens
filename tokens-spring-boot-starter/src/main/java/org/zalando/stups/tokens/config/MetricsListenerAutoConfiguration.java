package org.zalando.stups.tokens.config;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.zalando.stups.tokens.MetricsListener;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

/**
 * AutoConfiguration to expose metrics about requesting Tokens from
 * OAuth-Endpoints (TokenInfo, TokenCreation).
 * 
 * @author jbellmann
 *
 */
@Configuration
@ConditionalOnBean(MetricRegistry.class)
@EnableAsync
public class MetricsListenerAutoConfiguration {

    private final Logger log = LoggerFactory.getLogger(MetricsListenerAutoConfiguration.class);

    @Bean
    public TokensMetricsListener tokenMetricsListener(MetricRegistry registry) {
        log.info("Create 'TokensMetricsListener'.");
        return new TokensMetricsListener(registry);
    }

    static class TokensMetricsListener implements MetricsListener {

        private final Logger log = LoggerFactory.getLogger(TokensMetricsListener.class);

        private final MetricRegistry registry;

        public TokensMetricsListener(MetricRegistry registry) {
            this.registry = registry;
        }

        @Override
        @Async
        public void submitToTimer(String key, long time) {
            try {
                Timer timer = registry.timer(key);
                timer.update(time, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.warn("Unable to submit timer metric '" + key + "'", e);
            }

        }

    }

}
