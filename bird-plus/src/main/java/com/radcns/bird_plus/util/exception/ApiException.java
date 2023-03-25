package com.radcns.bird_plus.util.exception;

@SuppressWarnings("serial")
public class ApiException extends BirdPlusException {
	
	public ApiException(Result result) {
		super(result.message(), result.code());
	}
	
	@Override
	public int getResultCode() {
		return super.code;
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
