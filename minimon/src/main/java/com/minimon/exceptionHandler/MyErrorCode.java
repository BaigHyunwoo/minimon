package com.minimon.exceptionHandler;

public enum MyErrorCode {
	UNKNOWN(1),
	SQL(2),
	IO(3),
	FILE_NOT_FOUND(4),
	NUMBER_FORMAT(5),
	NULL_POINTER(6),
	INDEX_OUT_OF_BOUND(7),
	ILLEGAL_ARGUMENT(8),
	PARAM_IS_NULL(9),
	FUNCTION(10),
	SUCCESS(200);
	 
    private int errorCode;
 
    private MyErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
 
    public int getErrorCode() {
        return errorCode;
    }
}
