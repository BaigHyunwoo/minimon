package com.minimon.exception;

public class KillDriverProcessException extends RuntimeException {
    public KillDriverProcessException(Throwable t) {
        super(t);
    }

    public KillDriverProcessException() {
        super();
    }
}
