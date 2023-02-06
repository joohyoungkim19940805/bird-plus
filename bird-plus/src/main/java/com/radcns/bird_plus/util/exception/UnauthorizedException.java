package com.radcns.bird_plus.util.exception;

@SuppressWarnings("serial")
public class UnauthorizedException extends ApiException {
	public UnauthorizedException(String message) {
        super(101);
    }
}
