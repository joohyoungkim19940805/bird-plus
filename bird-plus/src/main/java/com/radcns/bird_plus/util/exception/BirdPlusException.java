package com.radcns.bird_plus.util.exception;

public abstract non-sealed class BirdPlusException extends RuntimeException implements CommonExceptionResult{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8263859706689473805L;
	protected final Result result;
	protected BirdPlusException(Result result){
		super(result.code() + " : " + result.message());
		this.result = result;
	}
	
	public enum Result implements CommonResultCode{
		
		_0(0, "처리에 성공하였습니다.", "SUCCESS"),
		_1(1, "처리에 실패하였습니다.", "SERVER RESPONSE SOMETHING WRONG"),
		_100(100, "다시 로그인을 시도해주십시오.", "TOKEN EXPIRED"),
		_101(101, "비활성화 된 계정입니다.\n인증 메일을 전송하였으니, 인증 후 다시 시도해주십시오.", "ACCOUNT IS DISABLED STATUS"),
		_102(102, "로그인 정보가 잘못되었습니다.", "INVALID ACCOUNT"), //"잘못된 비밀번호입니다.", "INVALID ACCOUNT PASSWORD"),
		_103(103, "로그인 정보가 잘못되었습니다.", "INVALID ACCOUNT"),
		_104(104, "비활성화 된 계정입니다.", "DISABLED ACCOUNT"),
		_105(105, "로그인 정보가 잘못되었습니다. 다시 로그인을 시도해주십시오.", "NOT SUPPORTED TOKEN"),
		_106(106, "호환되지 않은 계정 정보입니다. 다시 로그인을 시도해주십시오.", "NOT A VALID TOKEN"),
		_107(107, "인증 할 수 없는 계정입니다. 다시 로그인을 시도해주십시오.", "SIGNATURE VALIDATION FAILS"),
		_108(108, "해당 이메일로 된 계정은 존재하지 않습니다.", "PASSWORD CHANGE IS FAILS"),
		_110(110, "회원 가입에 실패하였습니다.", "ACCOUNT REGIST FAILED"),
		_200(200, "존재하지 않는 워크스페이스입니다.", "INVALID WORKSPACE"),
		_201(201, "해당 워크스페이스에 접근 할 권한이 없습니다.", "WORKSPACE ACCESS DEFINED"),
		_202(202, "해당 워크스페이스에서 허용하는 이메일이 아닙니다.", "WORKSPACE ACCESS EMAIL DEFINED"),
		_203(203, "이미 참여 중인 워크스페이스 입니다.", "ALREADY JOINED WORKSPACE"),
		_204(204, "해당 기능에 권한이 존재하지 않습니다.", "THIS FUNCTION ACCESS DEFINED"),
		_300(300, "존재하지 않는 방입니다.", "INVALID ROOM"),
		_301(301, "해당 방에 접근 할 권한이 없습니다.", "ROOM ACCESS DEFINED"),
		_302(302, "이미 존재하는 방입니다.", "ROOM ACCESS DEFINED"),
		_400(400, "삭제하려는 게시판이 존재하지 않습니다.", "INVALID NOTICE BOARD"),
		_500(500, "s3 upload failed generate key", "FAILED GENERATE KEY"),
		_501(501, "s3 upload failed signature key", "FAILED SIGNATURE KEY"),
		_502(502, "invalid signature key", "INVALID  SIGNATURE KEY"),
		_503(503, "invalid data", "INVALID DATA"),
		_504(504, "invalid cipher algorithm or padding", "INVALID CIPHER"),
		_505(505, "invalid key", "INVALID KEY"),
		_506(506, "s3 upload failed key encryption", "FAILED KEY ENCRYPTION"),
		_999(999, "처리에 실패하였습니다. 잠시 후 다시 시도해주십시오.", "SERVER NOT DEFINED THIS ERROR")
		;

		private int code;
		private String message;
		private String summary;
		private Result(int code, String message, String summary) {
			this.code = code;
			this.message = message;
			this.summary = summary;
		}
		@Override
		public int code() {
			return this.code;
		}
		@Override
		public String message() {
			return this.message;
		}
		@Override
		public String summary() {
			return this.summary;
		}
		@Override
		public Result withChangeMessage(String newMessage) {
			this.message = newMessage;
			return this;
		}
		@Override
		public String toString() {
			return """
					{"code":%d,"message":"%s","detail":"%s"}
					""".formatted(code,message,summary);
		}

	}

}
