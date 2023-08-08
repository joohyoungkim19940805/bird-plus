package com.radcns.bird_plus.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.Transient;

import com.radcns.bird_plus.entity.account.AccountEntity;

import lombok.Getter;

public abstract class DefaultEntity {

	@Getter
	@Transient
	Long createTime = null;
	
	@Getter
	@Transient
	Long updateTime = null;
	

	public void setCreateAt(LocalDateTime createAt) {
		this.setCreateAt(createAt);
		this.createTime = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	public void setUpdateAt(LocalDateTime updateAt) {
		this.setCreateAt(updateAt);
		this.createTime = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	public static void main(String a[]) {
		var t = new AccountEntity();
		t.setCreateAt(LocalDateTime.now());
		System.out.println(t);
		System.out.println(t.getCreateTime());
		
	}
}
