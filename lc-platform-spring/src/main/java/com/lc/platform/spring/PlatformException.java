package com.lc.platform.spring;

/**
 * 整个平台顶级异常类
 * @author chenjun
 *
 */
public abstract class PlatformException extends RuntimeException {

	private static final long serialVersionUID = -5143695406381565749L;
	
	protected int code;
	
	public PlatformException() {
		 super();
    }
	 
	public int getCode() {
		return code;
	}

	public PlatformException(String msg) {
		super(msg);
	}

	public PlatformException(String msg, Throwable cause) {
		super(msg, cause);
	}

	
}
