package com.radcns.bird_plus.entity.chatting;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.radcns.bird_plus.entity.DefaultFieldEntity;

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
@ToString(callSuper = true)
@Table(value="ch_workspace_members")
public class WorkspaceMembersEntity extends DefaultFieldEntity{
	
	@Id
	@Column("id")
	private Long id;
	
	@Column("workspace_id")
	private Long workspaceId;
	
	@Column("account_id")
	private Long accountId;
	
	public static class MyJoinedWorkspaceListDomain{
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
