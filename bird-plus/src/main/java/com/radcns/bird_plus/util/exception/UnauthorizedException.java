package com.radcns.bird_plus.util.exception;

@SuppressWarnings("serial")
public class UnauthorizedException extends BirdPlusException {
	
	
	public UnauthorizedException(int statusCode) {
		super(Error.valueOf("_"+statusCode).message(), statusCode);
	}
	
	@Override
	public int getStatusCode() {
		return super.statusCode;
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
