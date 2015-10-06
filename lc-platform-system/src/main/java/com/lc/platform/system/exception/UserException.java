package com.lc.platform.system.exception;

import com.lc.platform.commons.spring.MessageUtil;
import com.lc.platform.spring.PlatformException;

public class UserException extends PlatformException {

	private static final long serialVersionUID = 6144173168088711457L;

	public UserException(String msg) {
		super(msg);
	}

	public UserException(int code) {
		super(MessageUtil.getMessage(code+""));
		this.code = code;
	}
	
	public UserException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
