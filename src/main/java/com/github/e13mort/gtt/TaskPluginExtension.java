package com.github.e13mort.gtt;

public class TaskPluginExtension {

    static final long DEFAULT_THRESHOLD = 50L;

    private long threshold = DEFAULT_THRESHOLD;

    @SuppressWarnings("unused")
    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    long getThreshold() {
        return threshold;
    }
}
