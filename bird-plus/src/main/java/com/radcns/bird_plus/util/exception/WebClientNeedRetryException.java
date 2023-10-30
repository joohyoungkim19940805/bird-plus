package com.radcns.bird_plus.util.exception;

@SuppressWarnings("serial")
public class WebClientNeedRetryException extends BirdPlusException{
	private final int statusCode;
	
	private boolean isNeedRetry;
	
	public WebClientNeedRetryException(Result result, int statusCode) {
		super(result);
		this.statusCode = statusCode;
		this.isNeedRetry= true;
	}
	public WebClientNeedRetryException(Result result, int statusCode, Boolean isNeedRetry) {
		super(result);
		this.statusCode = statusCode;
		this.isNeedRetry = isNeedRetry == null ? true : isNeedRetry.booleanValue();
	}
	
	public int getStatusCode() {
		return this.statusCode;
	}
	
	public void toggleIsNeedRetry() {
		this.isNeedRetry = !this.isNeedRetry;
	}
	
	public void setIsNeedRetryEnd() {
		this.isNeedRetry = false;
	}
	
	public void setIsNeedRetryStart() {
		this.isNeedRetry = true;
	}
	
	@Override
	public Result getResult() {
		// TODO Auto-generated method stub
		return super.result;
	}
	@Override
	public Result getResult(int code) {
		// TODO Auto-generated method stub
		return Result.valueOf("_" + code);
	}

}
