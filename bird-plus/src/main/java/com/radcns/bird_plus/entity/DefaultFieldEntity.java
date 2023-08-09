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
import lombok.experimental.SuperBuilder;

@ToString
public abstract class DefaultFieldEntity {

    @Column("create_at")
    @CreatedDate
    @Setter
    @Getter
    private LocalDateTime createAt;
    
    @Column("create_by")
    @CreatedBy
    @Setter
    @Getter
    private String createBy;
    
    @Column("updated_at")
    @LastModifiedDate
    @Setter
    @Getter
    private LocalDateTime updatedAt;

    @Column("updated_by")
    @LastModifiedBy
    @Setter
    @Getter
    private String updatedBy;
	
	@Getter
	@Transient
	Long createMils = null;
	
	@Getter
	@Transient
	Long updateMils = null;
	

	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
		this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	public void setUpdateAt(LocalDateTime updateAt) {
		this.updatedAt = updateAt;
		this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

}
