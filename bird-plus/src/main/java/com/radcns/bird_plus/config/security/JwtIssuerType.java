package com.radcns.bird_plus.config.security;

public enum JwtIssuerType {
	ACCOUNT(32400), BOT(0), FORGOT_PASSWORD(1200);
	Integer second;
	JwtIssuerType(Integer second){
		this.second = second;
	}
	public Integer getSecond(){
		return this.second;
	}
	
}