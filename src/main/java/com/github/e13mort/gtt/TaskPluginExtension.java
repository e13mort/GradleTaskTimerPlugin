package com.github.e13mort.gtt;

public class TaskPluginExtension {

    static final long DEFAULT_THRESHOLD = 50L;

    private long thresholdMs = DEFAULT_THRESHOLD;

    @SuppressWarnings("unused")
    public void setThresholdMs(long thresholdMs) {
        this.thresholdMs = thresholdMs;
    }

    long getThresholdMs() {
        return thresholdMs;
    }
}
