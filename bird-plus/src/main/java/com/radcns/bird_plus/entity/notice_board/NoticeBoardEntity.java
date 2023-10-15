package com.radcns.bird_plus.entity.notice_board;

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
import lombok.With;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(value="no_notice_board")
public class NoticeBoardEntity implements NoticeBoardInheritsTable{
	@Id
	@Column("id")
	private Long id;
	
	@Column("account_id")
	private Long accountId;
	
	@Column("room_id")
	private Long roomId;
	
	@Column("workspace_id")
	private Long workspaceId;
	
	@Column("parent_group_id")
	private Long parentGroupId;
	
	@Column("order_sort")
	private Long orderSort;
	
	@Column("full_name")
	private String fullName;
	
	@Column("title")
	private String title;
	
    @Column("is_delete")
    private Boolean isDelete;
    
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
    
    @Column("group_id")
    private Long groupId;
    
	@Transient
	Long createMils;
	
	@Transient
	Long updateMils;
	
	@Override
	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
		this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	@Override
	public void setUpdateAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
		this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	@Override
	public Long getCreateMils() {
		if(this.createAt == null) {
			return null;
		}else if(this.createMils == null) {
			this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
		}
		return this.createMils; 
	}
	@Override
	public Long getUpdateMils() {
		if(this.updateAt == null) {
			return null;
		}else if(this.updateMils == null) {
			this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		}
		return this.updateMils; 
	}
	
	public static class NoticeBoardDomain{
		
	}
}
