package com.minimon.exception;

public class ResponseParsingException extends RuntimeException {
    public ResponseParsingException(Throwable t) {
        super(t);
    }

    public ResponseParsingException() {
        super();
    }
}
