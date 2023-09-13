package com.radcns.bird_plus.entity.account;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(value="ac_account_log")
public class AccountLogEntity{
	
    @Column("account_id")
	private Long accountId;
	
    @Column("ip")
	private String ip;
    
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
	public void setUpdatedAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
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
	public Long getUpdatedMils() {
		if(this.updateAt == null) {
			return null;
		}else if(this.updateMils == null) {
			this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		}
		return this.updateMils; 
	}
}
