package com.radcns.bird_plus.entity.account;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.radcns.bird_plus.config.security.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(value="ac_account")
public class AccountEntity {
	
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
    
    @Column("job_grade")
    private String jobGrade;
    
    @Column("department")
    private String department;
    
    @Column("create_at")
    @CreatedDate
    private LocalDateTime createAt;
    
    @Column("create_by")
    @CreatedBy
    private Long createBy;
    
    @Column("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column("updated_by")
    @LastModifiedBy
    private Long updatedBy;

	@Transient
	Long createMils = null;
	
	@Transient
	Long updateMils = null;
	
	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
		this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	public void setUpdatedAt(LocalDateTime updateAt) {
		this.updatedAt = updateAt;
		this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	public Long getCreateMils() {
		if(this.createAt == null) {
			return null;
		}else if(this.createMils == null) {
			this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
		}
		return this.createMils; 
	}
	public Long getUpdateMils() {
		if(this.updatedAt == null) {
			return null;
		}else if(this.updateMils == null) {
			this.updateMils = updatedAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		}
		return this.updateMils; 
	}
    
    public static class AccountDomain{
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
    }
    /**
     * 
     * @author kim.joohyoung
     * BasicUserDTO dto = BasicMapper.INSTANCE.convert(user);
     */
    @Mapper
    public interface AccountMapper{
    	AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    	AccountEntity entity(AccountDomain.ChangePasswordRequest vo);
    	AccountEntity entity(AccountDomain.AccountVerifyRequest vo);

    	AccountDomain.ChangePasswordRequest changePasswordRequest(AccountEntity entity);
    	
    	AccountDomain.AccountVerifyRequest accountVerifyRequest(AccountEntity entity);
    }
}