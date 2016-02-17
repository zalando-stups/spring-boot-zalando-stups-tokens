package org.zalando.stups.tokens.config;

import java.util.concurrent.TimeUnit;

public class CircuitBreakerConfiguration {

    private int errorThreshold = 5;

    private int timeout = 30;

    private int maxMulti = 40;

    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public CircuitBreakerConfiguration() {
        // default
    }

    public CircuitBreakerConfiguration(int errorThreshold, int timeout, int maxMulti, TimeUnit timeUnit) {
        this.errorThreshold = errorThreshold;
        this.timeout = timeout;
        this.maxMulti = maxMulti;
        this.timeUnit = timeUnit;
    }

    public CircuitBreakerConfiguration(int errorThreshold, int timeout, int maxMulti) {
        this(errorThreshold, timeout, maxMulti, TimeUnit.SECONDS);
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

}
