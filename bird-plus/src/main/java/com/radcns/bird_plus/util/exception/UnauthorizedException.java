package com.radcns.bird_plus.util.exception;

@SuppressWarnings("serial")
public class UnauthorizedException extends BirdPlusException {
	
	public UnauthorizedException(Result result) {
		super(result);
	}
	
	@Override
	public int getResultCode() {
		return super.code;
	}
	@Override
	public Result getResult() {
		// TODO Auto-generated method stub
		return Result.valueOf("_" + this.code);
	}
	@Override
	public Result getResult(int code) {
		// TODO Auto-generated method stub
		return Result.valueOf("_" + code);
	}

}
