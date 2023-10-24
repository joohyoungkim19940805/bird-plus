package com.radcns.bird_plus.entity.account;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.radcns.bird_plus.config.security.Role;
import com.radcns.bird_plus.config.security.TokenTemplate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@With
@ToString
@Table("ac_account")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountEntity implements TokenTemplate {
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

    @Column("update_at")
    @LastModifiedDate
    private LocalDateTime updateAt;

    @Column("update_by")
    @LastModifiedBy
    private Long updateBy;

    @Transient
    Long createMils;

    @Transient
    Long updateMils;

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
        this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
        this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public Long getCreateMils() {
        if (this.createAt == null) {
            return null;
        } else if (this.createMils == null) {
            this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        return this.createMils;
    }

    public Long getUpdateMils() {
        if (this.updateAt == null) {
            return null;
        } else if (this.updateMils == null) {
            this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        return this.updateMils;
    }

    public static class AccountDomain {
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

    @Override
    public String getIssuer() {
        // TODO Auto-generated method stub
        return this.accountName;
    }

    @Override
    public String getSubject() {
        // TODO Auto-generated method stub
        return this.email;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return this.fullName;
    }

    @Column("test_c")
    private Long testC;

    @Column("is_delete")
    private Boolean isDelete;
}