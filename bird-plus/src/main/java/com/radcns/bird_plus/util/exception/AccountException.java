package com.radcns.bird_plus.util.exception;

import io.jsonwebtoken.JwtException;

@SuppressWarnings("serial")
public class AccountException extends BirdPlusException {
	public AccountException(Result result) {
		super(result);
	}
	public AccountException(Result result, JwtException e) {
		super(result.withChangeMessage(e.getMessage()));
	}
	
	@Override
	public Result getResult() {
		// TODO Auto-generated method stub
		return super.result;
	}
	@Override
	public Result getResult(int status) {
		// TODO Auto-generated method stub
		return Result.valueOf("_"+status);
	}
}
