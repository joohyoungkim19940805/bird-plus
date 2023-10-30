package com.radcns.bird_plus.util.exception;


@SuppressWarnings("serial")
public class RoomException extends BirdPlusException{
	public RoomException(Result result) {
		super(result);
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
