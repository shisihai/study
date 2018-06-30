package com.traveler.print.utils;
/**
 * <p>功能描述：自定义异常类</p>
 *@author SShi11
 */
public class OperationException extends Exception {
	private static final long serialVersionUID = 1L;

	public OperationException() {
		super();
	}

	public OperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	/**
	 * <p>功能描述：非控制的异常</p>
	 *@param message
	 *@param cause
	 *作者：SShi11
	 *日期： Apr 11, 2017 9:59:24 AM
	 */
	public OperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public OperationException(String message) {
		super(message);
	}

	public OperationException(Throwable cause) {
		super(cause);
	}
	
}
