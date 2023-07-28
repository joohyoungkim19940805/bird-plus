package com.radcns.bird_plus.entity.account;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(value="cu_account_log")
public class AccountLogEntity {
	
	@Id
    @Column("id")
	private Long id;
	
    @Column("ip")
	private String ip;
	
    @Column("create_at")
    @CreatedDate
    private LocalDateTime createAt;
    
    @Column("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
}
