package com.radcns.bird_plus.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.radcns.bird_plus.config.security.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(value="cu_account")
public class AccountEntity {
	
    @Id
    @Column("id")
    private Long id;
    
    @Column("username")
    private String username;

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

    @Column("version")
    @Version
    private Long version;
    
    private List<Role> roles;


}