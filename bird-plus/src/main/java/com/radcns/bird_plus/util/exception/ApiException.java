package com.radcns.bird_plus.util.exception;

@SuppressWarnings("serial")
public class ApiException extends BirdPlusException {
	
	public ApiException(int code) {
		super(Error.valueOf("_"+code).message(), code);
	}
	
	@Override
	public int getErrorCode() {
		return super.code;
	}
	@Override
	public Error getError() {
		// TODO Auto-generated method stub
		return Error.valueOf("_"+this.code);
	}
	@Override
	public Error getError(int status) {
		// TODO Auto-generated method stub
		return Error.valueOf("_"+status);
	}
}
