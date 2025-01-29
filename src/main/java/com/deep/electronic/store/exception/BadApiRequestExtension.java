package com.deep.electronic.store.exception;

public class BadApiRequestExtension  extends RuntimeException{

	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadApiRequestExtension() {
	        super();
	    }

	public BadApiRequestExtension(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public BadApiRequestExtension(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BadApiRequestExtension(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BadApiRequestExtension(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	
}
