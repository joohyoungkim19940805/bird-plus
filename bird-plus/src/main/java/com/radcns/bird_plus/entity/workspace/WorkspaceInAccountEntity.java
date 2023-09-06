package com.radcns.bird_plus.entity.workspace;

import java.io.Serializable;
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
@With
@Table(value="wo_workspace_in_account")
public class WorkspaceInAccountEntity {
	
	@Id
	@Column("id")
	private Long id;
	
	@Column("workspace_id")
	private Long workspaceId;
	
	@Column("account_id")
	private Long accountId;
	
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
	Long createMils;
	
	@Transient
	Long updateMils;
	
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
	
	public static class WorkspaceMembersDomain{
		@Getter
		@Setter
		public static class MyJoinedWorkspaceListResponse{
			//@Cloumn("id")
			private Long workspaceId;
			
			private String workspaceName;
			
			private Boolean isEnabled;
			
			private List<String> accessFilter;
			
			private Boolean isFinallyPermit;
			
			private Long joinedCount;
		}
		@Getter
		@Setter
		public static class WorkspaceInAccountListResponse{
			private Long workspaceId;
			private String accountName;
			private String fullName;
			private String jobGrade;
			private String department;
		}
	}

/*
	@SuppressWarnings("serial")
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class WorkspaceMembersEntityPK implements Serializable{
		private Long workspaceId;
		
		private Long accountId;
	}
*/
}
