package com.radcns.bird_plus.entity.account.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountVo {
	private String email;
	private String password;
	private String newPassword;
	private String token;
}