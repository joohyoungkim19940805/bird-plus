package com.radcns.bird_plus.entity.workspace;

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

import java.util.List;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@ToString(callSuper = true)
@Table(value="wo_workspace")
public class WorkspaceEntity extends DefaultFieldEntity{
	
	@Id
	@Column("id")
	private Long id;
	
	@Column("workspace_name")
	private String workspaceName;
	
	@Column("is_enabled")
	private Boolean isEnabled;
	
	@Column("access_filter")
	private List<String> accessFilter;
	
	@Column("is_finally_permit")
	private Boolean isFinallyPermit;
	
	@Column("owner_account_id")
	private Long ownerAccountId;
	
	public static class WorkspaceDomain{
		@Getter
		@Setter
		public static class SearchWorkspaceListResponse{
			private Long id;
			
			private String workspaceName;
			
			private Boolean isEnabled;
			
			private List<String> accessFilter;
			
			private Boolean isFinallyPermit;
			
			private Long joinedCount;
		}
	}
	

}
