package com.minimon.exception;

public class SearchQueryParsingException extends RuntimeException {
    public SearchQueryParsingException(Throwable t) {
        super(t);
    }

    public SearchQueryParsingException() {
        super();
    }
}
