package com.radcns.bird_plus.config.security;

/**
 * PreAuthorize를 사용 하는 경우 ROLE_접두사가 필요
 * @author oozu1
 *
 */
public enum Role {
	ROLE_USER("ROLE_USER"), ROLE_ACCESS("ROLE_ACCESS");
	private String role;
	Role(String role){
		this.role = role;
	}
	@Override
	public String toString() {
		return this.role;
	}
}
