package com.jordansamhi.experiments.callgraphsoundness.from_other_tools.jicer.statistics;

public class Timer {
    private String title;
    private long start;
    private long time;
    private boolean running;

    public Timer(String title) {
        this.title = title;
        this.time = 0;
        this.running = false;
    }

    public synchronized void start() {
    }

    public synchronized void stop() {
    }

    public synchronized void setTime(long timeInMS) {
        this.time = timeInMS;
    }

    public synchronized long getTime() {
        return this.time;
    }

    public synchronized String getTimeAsString() {
        return "";
    }

    private String digits(long input, int num) {
        return "";
    }
}