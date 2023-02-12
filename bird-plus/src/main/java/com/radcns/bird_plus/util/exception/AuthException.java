package com.radcns.bird_plus.util.exception;

import io.jsonwebtoken.JwtException;

@SuppressWarnings("serial")
public class AuthException extends BirdPlusException {
	public AuthException(int code) {
		super(Error.valueOf("_"+code).message(), code);
	}
	public AuthException(int code, JwtException e) {
		super(Error.valueOf("_"+code).withChangeMessage(e.getMessage()).message(), code);
	}
	
	@Override
	public int getErrorCode() {
		return this.code;
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
