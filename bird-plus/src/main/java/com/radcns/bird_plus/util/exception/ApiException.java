package com.radcns.bird_plus.util.exception;

@SuppressWarnings("serial")
public class ApiException extends BirdPlusException {
	
	public ApiException(int status) {
		super(Error.valueOf("_"+status).message(), status);
	}
	
	@Override
	public int getErrorCode() {
		return super.status;
	}
	@Override
	public Error getError() {
		// TODO Auto-generated method stub
		return Error.valueOf("_"+this.status);
	}
	@Override
	public Error getError(int status) {
		// TODO Auto-generated method stub
		return Error.valueOf("_"+status);
	}
}
