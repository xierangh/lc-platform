package com.lc.platform.spring;

public class FileUploadException extends PlatformException {

	
	private static final long serialVersionUID = -8025479973121392610L;

	public FileUploadException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public FileUploadException(String msg) {
		super(msg);
	}
	
}
