package com.radcns.bird_plus.util.exception;

import com.radcns.bird_plus.util.ExceptionCodeConstant;

public abstract class BirdPlusException extends RuntimeException implements ExceptionCodeConstant{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8263859706689473805L;
	protected final int status;

	BirdPlusException(String message, int status){
		super(message);
		this.status = status;
	}
	public abstract int getErrorCode();
}
