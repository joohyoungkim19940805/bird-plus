package com.radcns.bird_plus.util.exception;

import io.jsonwebtoken.JwtException;

@SuppressWarnings("serial")
public class AuthException extends BirdPlusException {
	public AuthException(int code) {
		super(Result.valueOf("_"+code).message(), code);
	}
	public AuthException(int code, JwtException e) {
		super(Result.valueOf("_"+code).withChangeMessage(e.getMessage()).message(), code);
	}
	
	@Override
	public int getResultCode() {
		return this.code;
	}
	@Override
	public Result getResult() {
		// TODO Auto-generated method stub
		return Result.valueOf("_"+this.code);
	}
	@Override
	public Result getResult(int status) {
		// TODO Auto-generated method stub
		return Result.valueOf("_"+status);
	}
}
