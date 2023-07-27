package com.radcns.bird_plus.entity.account.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountVo {
	private String email;
	private String password;
	private String newPassword;
	private String token;
}