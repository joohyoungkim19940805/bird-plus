package com.radcns.bird_plus.entity.account;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.radcns.bird_plus.config.security.Role;
import com.radcns.bird_plus.entity.DefaultEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(value="cu_account")
public class AccountEntity extends DefaultEntity {
	
    @Id
    @Column("id")
    private Long id;
    
    @Column("account_name")
    private String accountName;

    @Column("password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @Column("is_enabled")
    private Boolean isEnabled;
    
    @Column("create_at")
    @CreatedDate
    private LocalDateTime createAt;
    
    @Column("create_by")
    @CreatedBy
    private String createBy;
    
    @Column("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column("updated_by")
    @LastModifiedBy
    private String updatedBy;
    
    @Column("roles")
    private List<Role> roles;

    @Column("is_different_ip")
    private Boolean isDifferentIp;
    
    @Column("is_first_login")
    private Boolean isFirstLogin;
    
    @Column("full_name")
    private String fullName;
    
    @Column("email")
    private String email;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequest {
    	private String email;
    	private String password;
    	private String newPassword;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountVerifyRequest {
    	private String email;
    }
    
    /**
     * 
     * @author kim.joohyoung
     * BasicUserDTO dto = BasicMapper.INSTANCE.convert(user);
     */
    @Mapper
    public interface AccountMapper{
    	AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    	AccountEntity entity(ChangePasswordRequest vo);
    	AccountEntity entity(AccountVerifyRequest vo);

    	ChangePasswordRequest changePasswordRequest(AccountEntity entity);
    	
    	AccountVerifyRequest accountVerifyRequest(AccountEntity entity);
    }
}