package com.radcns.bird_plus.util.exception;

import com.radcns.bird_plus.util.ExceptionCodeConstant;

public abstract class BirdPlusException extends RuntimeException implements ExceptionCodeConstant{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8263859706689473805L;
	protected final int statusCode;

	BirdPlusException(String message, int statusCode){
		super(message);
		this.statusCode = statusCode;
	}
	public abstract int getStatusCode();
}
