package com.lc.platform.system.exception;

import com.lc.platform.commons.spring.MessageUtil;

public class UserNameUniqueException extends UserException {
	
	private static final long serialVersionUID = -2572941963534059127L;

	public UserNameUniqueException(String userName) {
		super(MessageUtil.getMessage("14002",new Object[]{userName}));
		this.code = 14002;
	}

	public UserNameUniqueException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
