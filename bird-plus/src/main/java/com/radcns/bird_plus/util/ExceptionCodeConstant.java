package com.radcns.bird_plus.util;

public interface ExceptionCodeConstant {
	
	public enum Error {
		_100(100, "token expired", "JWT_TOKEN_EXPIRED"),
		_101(101, "access denied", "ACCOUNT_IS_DISABLED_STATUS"),
		_102(102, "invalid account password", "INVALID_ACCOUNT_PASSWORD"),
		_103(103, "invalid account id", "INVALID_ACCOUNT"),
		_104(104, "account is disabled", "DISABLED ACCOUNT"),
		_105(105, "this token is not supported", "NOT SUPPORTED TOKEN"),
		_106(106, "this is not a valid token", "NOT A VALID TOKEN"),
		_107(107, "signature validation fails token", "SIGNATURE VALIDATION FAILS TOKEN")
		;
		private int code;
		private String message;
		private String type;
		Error(int code, String message, String type) {
			this.code=code;
			this.message=message;
			this.type=type;
		}
		public int code() {
			return this.code;
		}
		public String message() {
			return this.message;
		}
		public String type() {
			return this.type;
		}
		public Error withChangeMessage(String newMessage) {
			this.message = newMessage;
			return this;
		}
		@Override
		public String toString() {
			return """
					{"code":%d,"message":"%s","detail":"%s"}
					""".formatted(code,message,type);
		}
	}

	Error getError();
	Error getError(int code);
}
