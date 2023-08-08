package com.radcns.bird_plus.config.security;

import java.time.Duration;

public enum JwtIssuerType {
	ACCOUNT(32400), BOT(0), FORGOT_PASSWORD(1200), ACCOUNT_VERIFY(1200);
	Integer second;
	JwtIssuerType(Integer second){
		this.second = second;
	}
	public Integer getSecond(){
		return this.second;
	}
	public Duration getDuration() {
		return Duration.ofSeconds(this.second);
	}
}