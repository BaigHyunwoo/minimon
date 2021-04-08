package com.minimon.exception;

public class MonitoringExecutionException extends RuntimeException {
    public MonitoringExecutionException(Throwable t) {
        super(t);
    }

    public MonitoringExecutionException() {
        super();
    }
}
