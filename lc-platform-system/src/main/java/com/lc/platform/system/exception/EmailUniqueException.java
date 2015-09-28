package com.lc.platform.system.exception;

import com.lc.platform.commons.spring.MessageUtil;

public class EmailUniqueException extends UserException {
	
	private static final long serialVersionUID = -2572941963534059127L;

	public EmailUniqueException(String email) {
		super(MessageUtil.getMessage("14001",new Object[]{email}));
		this.code = 14001;
	}

	public EmailUniqueException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
