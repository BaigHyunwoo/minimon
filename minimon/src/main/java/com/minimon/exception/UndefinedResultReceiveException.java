package com.minimon.exception;

public class UndefinedResultReceiveException extends RuntimeException {
    public UndefinedResultReceiveException(Throwable t) {
        super(t);
    }

    public UndefinedResultReceiveException() {
        super();
    }
}
