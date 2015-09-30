package com.lc.platform.system.exception;

import com.lc.platform.spring.PlatformException;

public class DictException extends PlatformException {

	private static final long serialVersionUID = -3994966929504672553L;

	public DictException(String msg) {
		super(msg);
	}

	public DictException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
