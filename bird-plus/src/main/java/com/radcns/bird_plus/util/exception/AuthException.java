package com.radcns.bird_plus.util.exception;

import io.jsonwebtoken.JwtException;

@SuppressWarnings("serial")
public class AuthException extends BirdPlusException {
	public AuthException(int status) {
		super(Error.valueOf("_"+status).message(), status);
	}
	public AuthException(int status, JwtException e) {
		super(Error.valueOf("_"+status).withChangeMessage(e.getMessage()).message(), status);
	}
	
	@Override
	public int getErrorCode() {
		return this.status;
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
