package com.radcns.bird_plus.entity.room;

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
@ToString
@With
@Table(value="ro_room_in_account")
public class RoomInAccountEntity {
	@Id
	@Column("id")
	private Long id;
	
	@Column("room_id")
	private Long roomId;
	
	@Column("account_id")
	private Long accountId;
	
	@Column("order_sort")
	private Long orderSort;
	
	@Column("workspace_id")
	private Long workspaceId;
	
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

	@Transient
	Long createMils;
	
	@Transient
	Long updateMils;
	
	@Transient
	RoomInAccountDomain.RoomJoinedAccountResponse roomJoinedAccountResponse;
	
	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
		this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	public void setUpdateAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
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
		if(this.updateAt == null) {
			return null;
		}else if(this.updateMils == null) {
			this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		}
		return this.updateMils; 
	}
	
	public static class RoomInAccountDomain{
		@Getter
		@Setter
		@Builder
		public static class MyJoinedRoomListResponse{
			private Long id;
			private Long roomId;
			private List<String> roomCode;
			private String roomName;
			private Boolean isEnabled;
			private Long workspaceId;
			private Long joinedCount;
			private Long orderSort;
			private RoomType roomType;
		}
		
		@Getter
		@Setter
		public static class CreateRoomInAccountRequest{
			private Long workspaceId;
			private Long roomId;
			private RoomType roomType;
			private String accountName;
			private String fullName;
		}
		
		@Getter
		@Setter
		@Builder
		public static class RoomJoinedAccountResponse{
			private Long roomId;
			private String roomName;
			private String accountName;
			private String fullName;
			private String job_grade;
			private String department;
			private RoomType roomType;
			private Long createMils;
			private Long updateMils;
		}
	}
}
