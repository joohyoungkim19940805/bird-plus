package com.radcns.bird_plus.entity.chatting;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@With
@Table(value="ch_chatting")
public class ChattingEntity {
	
	@Id
	@Column("id")
	private Long id;
	
    @Column("account_id")
	private Long accountId;
    
    @Column("account_name")
    private String accountName;
    
    @Column("room_id")
	private Long roomId;
    
    @Column(value="chatting")
    private String chatting;
    
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
    
    @Column("is_delete")
    private Boolean isDelete;
}
