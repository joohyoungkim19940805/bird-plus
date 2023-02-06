package com.radcns.bird_plus.config.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private Long userId;
    private String token;
    private Date issuedAt;
    private Date expiresAt;
}
