package com.minimon.exception;

public class UndefinedDriverException extends RuntimeException {
    public UndefinedDriverException(Throwable t) {
        super(t);
    }

    public UndefinedDriverException() {
        super();
    }
}
