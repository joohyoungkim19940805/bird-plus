package com.radcns.bird_plus.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

import com.radcns.bird_plus.entity.account.AccountEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import lombok.experimental.SuperBuilder;

//default entity 적용 안되므로 주석처리
/*
@ToString
@Setter
@Getter
public abstract class DefaultFieldEntity {

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
}
*/
