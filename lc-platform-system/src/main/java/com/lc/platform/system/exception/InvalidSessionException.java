package com.lc.platform.system.exception;

import com.lc.platform.commons.spring.MessageUtil;

public class InvalidSessionException extends UserException {
	
	private static final long serialVersionUID = 8001743932443600101L;

	public InvalidSessionException() {
		super(MessageUtil.getMessage(14013));
		this.code = 14013;
	}

	public InvalidSessionException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
