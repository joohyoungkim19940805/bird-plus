package com.radcns.bird_plus.entity.notice_board;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(value="no_notice_board_group")
public class NoticeBoardGroupEntity extends NoticeBoardEntity{
	
	//@Id
	@Column("group_id")
	private Long groupId;
	
	public static class NoticeBoardGroupDomain{
		
	}
	
}
