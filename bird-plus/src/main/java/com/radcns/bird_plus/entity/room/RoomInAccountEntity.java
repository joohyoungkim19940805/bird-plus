package com.radcns.bird_plus.entity.room;

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
@ToString(callSuper = true)
@With
@Table(value="ro_room_in_account")
public class RoomInAccountEntity extends DefaultFieldEntity{
	@Id
	@Column("id")
	private Long id;
	
	@Column("room_id")
	private Integer roomId;
	
	@Column("account_id")
	private Integer accountId;
	
	
	public static class RoomInAccountDomain{
		@Getter
		@Setter
		public static class MyJoinedRoomListResponse{
			private Long id;
			private String roomCode;
			private String roomName;
			private Boolean isEnabled;
			private Long workspaceId;
			private Long joinedCount;
		}
	}
}
