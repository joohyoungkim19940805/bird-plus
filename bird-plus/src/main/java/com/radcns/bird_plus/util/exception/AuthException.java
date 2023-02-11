package com.radcns.bird_plus.util.exception;

import io.jsonwebtoken.JwtException;

@SuppressWarnings("serial")
public class AuthException extends BirdPlusException {
	public AuthException(int statusCode) {
		super(Error.valueOf("_"+statusCode).message(), statusCode);
	}
	public AuthException(int statusCode, JwtException e) {
		super(Error.valueOf("_"+statusCode).withChangeMessage(e.getMessage()).message(), statusCode);
	}
	
	@Override
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
