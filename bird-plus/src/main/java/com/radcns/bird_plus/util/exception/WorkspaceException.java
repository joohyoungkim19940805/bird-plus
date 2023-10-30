package com.radcns.bird_plus.util.exception;


@SuppressWarnings("serial")
public class WorkspaceException extends BirdPlusException {
	public WorkspaceException(Result result) {
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
