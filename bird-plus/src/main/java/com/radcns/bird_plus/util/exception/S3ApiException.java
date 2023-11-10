package com.radcns.bird_plus.util.exception;


@SuppressWarnings("serial")
public class S3ApiException extends BirdPlusException {
	public S3ApiException(Result result) {
		super(result);
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