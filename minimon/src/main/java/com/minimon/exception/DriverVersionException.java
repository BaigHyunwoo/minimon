package com.minimon.exception;

public class DriverVersionException extends RuntimeException {
    public DriverVersionException(Throwable t) {
        super(t);
    }

    public DriverVersionException() {
        super();
    }
}
