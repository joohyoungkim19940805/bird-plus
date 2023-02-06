package com.radcns.bird_plus.util;

public interface ExceptionCodeConstant {
	public enum Error {
		_100(100, "Token Expired", "JWT_TOKEN_EXPIRED"),
		_101(101, "Access Denied", "ACCOUNT_IS_DISABLED_STATUS"),
		_102(102, "Invalid Account Password", "INVALID_ACCOUNT_PASSWORD"),
		_103(103, "Invalid Account Id Is Not Registered", "INVALID_ACCOUNT")
		;
		private int code;
		private String message;
		private String detail;
		Error(int code, String message, String detail) {
			this.code=code;
			this.message=message;
			this.detail=detail;
		}
		public int code() {
			return this.code;
		}
		public String message() {
			return this.message;
		}
		public String detail() {
			return this.detail;
		}
		@Override
		public String toString() {
			return """
					{"code":%d,"message":"%s","detail":"%s"}
					""".formatted(code,message,detail);
		}
	}
	Error getError();
	Error getError(int code);
}
