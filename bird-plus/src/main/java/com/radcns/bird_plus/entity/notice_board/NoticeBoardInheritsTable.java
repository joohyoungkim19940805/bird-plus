package com.radcns.bird_plus.entity.notice_board;

import java.time.LocalDateTime;

public interface NoticeBoardInheritsTable {
	
	Long getId();
	void setId(Long id);
	NoticeBoardInheritsTable withId(Long id);
	
	Long getGroupId();
	void setGroupId(Long groupId);
	NoticeBoardInheritsTable withGroupId(Long groupId);
	
	Long getAccountId();
	void setAccountId(Long accountId);
	NoticeBoardInheritsTable withAccountId(Long accountId);
	
	Long getRoomId();
	void setRoomId(Long roomId);
	NoticeBoardInheritsTable withRoomId(Long roomId);
	
	Long getWorkspaceId();
	void setWorkspaceId(Long workspaceId);
	NoticeBoardInheritsTable withWorkspaceId(Long workspaceId);
	
	Long getParentGroupId();
	void setParentGroupId(Long parentGroupId);
	NoticeBoardInheritsTable withParentGroupId(Long parentGroupId);
	
	Long getOrderSort();
	void setOrderSort(Long orderSort);
	NoticeBoardInheritsTable withOrderSort(Long orderSort);
	
	String getFullName();
	void setFullName(String fullName);
	NoticeBoardInheritsTable withFullName(String fullName);
	
	String getTitle();
	void setTitle(String title);
	NoticeBoardInheritsTable withTitle(String title);
	
	Boolean getIsDelete();
	void setIsDelete(Boolean isDelete);
	NoticeBoardInheritsTable withIsDelete(Boolean isDelete);
	
	LocalDateTime getCreateAt();
	void setCreateAt(LocalDateTime createAt);
	NoticeBoardInheritsTable withCreateAt(LocalDateTime createAt);
	
	Long getCreateBy();
	void setCreateBy(Long createBy);
	NoticeBoardInheritsTable withCreateBy(Long createBy);
	
	LocalDateTime getUpdateAt();
	void setUpdateAt(LocalDateTime updateAt);
	NoticeBoardInheritsTable withUpdateAt(LocalDateTime updateAt);
	
	Long getUpdateBy();
	void setUpdateBy(Long updateBy);
	NoticeBoardInheritsTable withUpdateBy(Long updateBy);
	
	Long getCreateMils();
	void setCreateMils(Long createMils);
	NoticeBoardInheritsTable withCreateMils(Long createMils);
	
	Long getUpdateMils();
	void setUpdateMils(Long updateMils);
	NoticeBoardInheritsTable withUpdateMils(Long updateMils);
}
