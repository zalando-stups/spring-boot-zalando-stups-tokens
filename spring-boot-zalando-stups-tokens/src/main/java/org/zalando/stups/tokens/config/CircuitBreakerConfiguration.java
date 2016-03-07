package org.zalando.stups.tokens.config;

import java.util.concurrent.TimeUnit;

/**
 * Circuit-Breaker Configuration used for refresher and verifier.
 * 
 * @author jbellmann
 *
 */
public class CircuitBreakerConfiguration {

    /**
     * Threshold when the CB should switch to OPEN.
     */
    private int errorThreshold = 5;

    /**
     * Timeout-period for the CB.
     */
    private int timeout = 30;

    /**
     * Maximum factor for timeout-period of the CB.
     */
    private int maxMulti = 40;

    /**
     * {@link TimeUnit} for the timeout-period.
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * Name of the CB.
     */
    private String name;

    public CircuitBreakerConfiguration() {
        // default
    }

    public CircuitBreakerConfiguration(String name) {
        this(5, 30, 40, TimeUnit.SECONDS, name);
    }

    public CircuitBreakerConfiguration(int errorThreshold, int timeout, int maxMulti, TimeUnit timeUnit, String name) {
        this.errorThreshold = errorThreshold;
        this.timeout = timeout;
        this.maxMulti = maxMulti;
        this.timeUnit = timeUnit;
        this.name = name;
    }

    public CircuitBreakerConfiguration(int errorThreshold, int timeout, int maxMulti, String name) {
        this(errorThreshold, timeout, maxMulti, TimeUnit.SECONDS, name);
    }

    public int getErrorThreshold() {
        return errorThreshold;
    }

    public void setErrorThreshold(int errorThreshold) {
        this.errorThreshold = errorThreshold;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxMulti() {
        return maxMulti;
    }

    public void setMaxMulti(int maxMulti) {
        this.maxMulti = maxMulti;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
