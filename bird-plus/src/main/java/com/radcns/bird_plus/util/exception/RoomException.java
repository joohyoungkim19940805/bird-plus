package com.radcns.bird_plus.util.exception;

import io.jsonwebtoken.JwtException;

@SuppressWarnings("serial")
public class RoomException extends BirdPlusException {
	public RoomException(Result result) {
		super(result.message(), result.code());
	}
	public RoomException(Result result, JwtException e) {
		super(result.withChangeMessage(e.getMessage()).message(), result.code());
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
