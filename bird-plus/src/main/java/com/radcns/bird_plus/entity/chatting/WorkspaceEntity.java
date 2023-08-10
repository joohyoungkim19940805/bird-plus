package com.radcns.bird_plus.entity.chatting;

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

import java.util.List;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Table(value="ch_chatting")
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
	
}
