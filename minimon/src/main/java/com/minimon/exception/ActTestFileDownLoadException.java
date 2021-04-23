package com.minimon.exception;

public class ActTestFileDownLoadException extends RuntimeException {
    public ActTestFileDownLoadException(Throwable t) {
        super(t);
    }

    public ActTestFileDownLoadException() {
        super();
    }
}
