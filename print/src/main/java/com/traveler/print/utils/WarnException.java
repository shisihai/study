package com.traveler.print.utils;
/**
 * <p>功能描述：自定义异常类</p>
 *@author SShi11
 */
public class WarnException extends OperationException {
	private static final long serialVersionUID = 1L;

	public WarnException() {
		super();
	}

	public WarnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public WarnException(String message, Throwable cause) {
		super(message, cause);
	}

	public WarnException(String message) {
		super(message);
	}

	public WarnException(Throwable cause) {
		super(cause);
	}
	
}
