package com.radcns.bird_plus.entity.room;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.radcns.bird_plus.entity.DefaultFieldEntity;
import com.radcns.bird_plus.entity.room.constant.RoomType;

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
@Table(value="ro_room")
public class RoomEntity extends DefaultFieldEntity{

	@Id
	@Column("id")
	private Long id;
	
	@Column("room_code")
	private String roomCode;
	
	@Column("room_name")
	private String roomName;
	
	@Column("is_enabled")
	private Boolean isEnabled;
	
	@Column("workspace_id")
	private Long workspaceId;
	
	@Column("room_type")
	private RoomType roomType;
	
}
