package com.minimon.exceptionHandler;

import java.io.PrintStream;
import java.io.PrintWriter;

public class MyException extends Exception{

	private static final long serialVersionUID = 4055391465873063328L;
	
	private String className;
	
	private int errorCode = 0;
	

	public MyException(){
		super();
	}
	
	public MyException(String msg, int errorCode){
		super(msg);
		this.errorCode = errorCode;
	}
	
	public MyException(String msg,String className){
		super(msg);
		this.className = className;
	}
	
	public MyException(String msg,String className, int errorCode){
		super(msg);
		this.className = className;
		this.errorCode = errorCode;
	}
    
	public MyException(Throwable cause) {
      super(cause);
    }
	
	public MyException(Throwable cause, StackTraceElement[] stackTrace) {
	      super(cause);
	      super.setStackTrace(stackTrace);
	}
	
    public MyException(String message, Throwable cause) {
        super(message, cause);
    }


	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	@Override 
	public StackTraceElement[] getStackTrace() {
		return super.getStackTrace();
	}

	@Override 
	public synchronized Throwable fillInStackTrace() {
	  return this;
	}
	
	@Override
   public String getLocalizedMessage() {
      return super.getLocalizedMessage();
   }
   
   @Override
   public synchronized Throwable getCause() {
      return super.getCause();
   }
   @Override
   public synchronized Throwable initCause(Throwable cause) {
      return super.initCause(cause);
   }
   @Override
   public void printStackTrace(PrintStream s) {
      super.printStackTrace(s);
   }
   
   @Override
   public void printStackTrace() {
      super.printStackTrace();
   }
   
   @Override
   public void printStackTrace(PrintWriter s) {
      super.printStackTrace(s);
   }
   
   @Override
   public String getMessage() {
      return super.getMessage();
   }
   
   @Override
   public void setStackTrace(StackTraceElement[] stackTrace) {
      super.setStackTrace(stackTrace);
   }

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
