package com.radcns.bird_plus.util.exception;

@SuppressWarnings("serial")
public class UnauthorizedException extends BirdPlusException {
	
	
	public UnauthorizedException(int status) {
		super(Error.valueOf("_"+status).message(), status);
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
	public Error getError(int code) {
		// TODO Auto-generated method stub
		return Error.valueOf("_"+code);
	}

}
