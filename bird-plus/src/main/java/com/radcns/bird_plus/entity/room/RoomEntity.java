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

import com.radcns.bird_plus.config.security.Role;
import com.radcns.bird_plus.config.security.TokenTemplate;
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
@With
@Table(value="ro_room")
public class RoomEntity implements TokenTemplate{

	@Id
	@Column("id")
	private Long id;
	
	@Column("room_code")
	private List<String> roomCode;
	
	@Column("room_name")
	private String roomName;
	
	@Column("is_enabled")
	private Boolean isEnabled;
	
	@Column("workspace_id")
	private Long workspaceId;
	
	@Column("room_type")
	private RoomType roomType;
	
    @Column("create_at")
    @CreatedDate
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();
    
    @Column("create_by")
    @CreatedBy
    private Long createBy;
    
    @Column("updated_at")
    @LastModifiedDate
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

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
	
	@Transient
	@Setter
	String roomCodeIssuerName = null;
	
	@Override
	public String getIssuer() {
		// TODO Auto-generated method stub
		return String.valueOf(this.id);
	}
	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return this.roomName;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.roomCodeIssuerName;
	}
	@Override
	public List<Role> getRoles() {
		// TODO Auto-generated method stub
		return List.of(Role.ROLE_BOT);
	}
	
}
