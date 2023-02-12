package com.radcns.bird_plus.util;

public interface ExceptionCodeConstant {
	
	public enum Error {
		_100(100, "token expired", "JWT TOKEN EXPIRED"),
		_101(101, "access denied", "ACCOUNT IS DISABLED STATUS"),
		_102(102, "invalid account password", "INVALID ACCOUNT PASSWORD"),
		_103(103, "invalid account id", "INVALID ACCOUNT"),
		_104(104, "account is disabled", "DISABLED ACCOUNT"),
		_105(105, "this token is not supported", "NOT SUPPORTED TOKEN"),
		_106(106, "this is not a valid token", "NOT A VALID TOKEN"),
		_107(107, "signature validation fails token", "SIGNATURE VALIDATION FAILS TOKEN"),
		_999(999, "unknown error", "SERVER NOT DEFINED THIS ERROR")
		;
		private int status;
		private String message;
		private String summary;
		Error(int status, String message, String summary) {
			this.status=status;
			this.message=message;
			this.summary=summary;
		}
		public int status() {
			return this.status;
		}
		public String message() {
			return this.message;
		}
		public String summary() {
			return this.summary;
		}
		public Error withChangeMessage(String newMessage) {
			this.message = newMessage;
			return this;
		}
		@Override
		public String toString() {
			return """
					{"code":%d,"message":"%s","detail":"%s"}
					""".formatted(status,message,summary);
		}
	}

	Error getError();
	Error getError(int code);
}
