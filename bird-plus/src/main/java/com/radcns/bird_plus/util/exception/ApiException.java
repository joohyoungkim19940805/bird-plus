package com.radcns.bird_plus.util.exception;

import com.radcns.bird_plus.util.ExceptionCodeConstant;

@SuppressWarnings("serial")
public class ApiException extends RuntimeException implements ExceptionCodeConstant {

	private final int statusCode;
	
	public ApiException(int statusCode) {
		super(Error.valueOf("_"+statusCode).message());
		this.statusCode = statusCode;
	}
	public int getStatusCode() {
		return this.statusCode;
	}
	@Override
	public Error getError() {
		// TODO Auto-generated method stub
		return Error.valueOf("_"+this.statusCode);
	}
	@Override
	public Error getError(int code) {
		// TODO Auto-generated method stub
		return Error.valueOf("_"+code);
	}
}
