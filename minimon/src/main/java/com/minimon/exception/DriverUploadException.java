package com.minimon.exception;

public class DriverUploadException extends RuntimeException {
    public DriverUploadException(Throwable t) {
        super(t);
    }

    public DriverUploadException() {
        super();
    }
}
