package com.ryaltech.utils.spring.encryption;

public class UnsupportedFileException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedFileException() {
		super();		
	}

	public UnsupportedFileException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public UnsupportedFileException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UnsupportedFileException(String arg0) {
		super(arg0);
	}

	public UnsupportedFileException(Throwable arg0) {
		super(arg0);
	}
	

}
