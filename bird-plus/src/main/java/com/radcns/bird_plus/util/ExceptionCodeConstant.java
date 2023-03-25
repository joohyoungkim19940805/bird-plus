package com.radcns.bird_plus.util;

public interface ExceptionCodeConstant {
	
	public enum Result {
		_00(0, "처리에 성공하였습니다.", "SUCCESS"),
		_01(1, "처리에 실패하였습니다.", "SERVER RESPONSE SOMETHING WRONG"),
		_100(100, "다시 로그인을 시도해주십시오.", "JWT TOKEN EXPIRED"),
		_101(101, "해당 기능에 권한이 없습니다.", "ACCOUNT IS DISABLED STATUS"),
		_102(102, "잘못된 비밀번호입니다.", "INVALID ACCOUNT PASSWORD"),
		_103(103, "존재하지 않는 계정입니다.", "INVALID ACCOUNT"),
		_104(104, "비활성화 된 계정입니다.", "DISABLED ACCOUNT"),
		_105(105, "로그인 정보가 잘못되었습니다. 다시 로그인을 시도해주십시오.", "NOT SUPPORTED TOKEN"),
		_106(106, "호환되지 않은 계정 정보입니다. 다시 로그인을 시도해주십시오.", "NOT A VALID TOKEN"),
		_107(107, "인증 할 수 없는 계정입니다. 다시 로그인을 시도해주십시오.", "SIGNATURE VALIDATION FAILS"),
		_110(110, "회원 가입에 실패하였습니다.", "ACCOUNT REGIST FAILED"),
		_999(999, "처리에 실패하였습니다. 잠시 후 다시 시도해주십시오.", "SERVER NOT DEFINED THIS ERROR")
		;
		private int code;
		private String message;
		private String summary;
		Result(int code, String message, String summary) {
			this.code=code;
			this.message=message;
			this.summary=summary;
		}
		public int code() {
			return this.code;
		}
		public String message() {
			return this.message;
		}
		public String summary() {
			return this.summary;
		}
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

	Result getResult();
	Result getResult(int code);
}
