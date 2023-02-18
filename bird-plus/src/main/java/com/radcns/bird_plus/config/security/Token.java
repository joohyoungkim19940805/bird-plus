package com.radcns.bird_plus.config.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Token{
    private Long userId;
    private String token;
    private Date issuedAt;
    private Date expiresAt;
    private Boolean isDifferentIp;
    private Boolean isFirstLogin;
}
