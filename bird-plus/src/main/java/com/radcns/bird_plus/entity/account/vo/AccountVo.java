package com.radcns.bird_plus.entity.account.vo;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountVo {
	private String email;
	private String password;
	private String token;
}