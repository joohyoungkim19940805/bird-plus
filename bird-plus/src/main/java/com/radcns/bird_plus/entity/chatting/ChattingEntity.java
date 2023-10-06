package com.radcns.bird_plus.entity.chatting;

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

import io.r2dbc.postgresql.codec.Json;
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
@Table(value="ch_chatting")
public class ChattingEntity {
	
	@Id
	@Column("id")
	private Long id;
	
    @Column("account_id")
	private Long accountId;
    
    @Column("account_name")
    private String accountName;
    
    @Column("room_id")
	private Long roomId;
    
    @Column("workspace_id")
    private Long workspaceId;
    
    @Column(value="chatting")
    private Json chatting;
    
    @Column("is_delete")
    private Boolean isDelete;

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

    @Column("update_by_array")
    private List<Long> updateByArray;
    
    @Column("update_at_array")
    private List<LocalDateTime> updateAtArray;
    
    @Transient
    List<Long> updateMilsArray;
    
	@Transient
	Long createMils;
	
	@Transient
	Long updateMils;
	
	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
		this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	public void setUpdatedAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
		this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	public void setUpdateAtArray(List<LocalDateTime> updateAtArray) {
		this.updateAtArray = updateAtArray;
		this.updateMilsArray = updateAtArray.stream().map(e->e.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).toList();
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
	public List<Long> getUpdateMilsArray(){
		if(this.updateAtArray == null) {
			return null;
		}else if(this.updateMilsArray == null) {
			this.updateMilsArray = updateAtArray.stream().map(e->e.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).toList();
		}
		return this.updateMilsArray;
	}
	
	public void setChatting(String chatting) {
		this.chatting = Json.of(chatting);
	}

	public String getChatting() {
		return this.chatting.asString();
	}
	
	public static class ChattingDomain{
		
		@Builder(toBuilder = true)
		@Getter
		@Setter
		@NoArgsConstructor
		@AllArgsConstructor
		@ToString
		@With
		public static class ChattingResponse{
			private Long id;
			private Long roomId;
			private Long workspaceId;
			private Json chatting;
			private LocalDateTime createAt;
			private LocalDateTime updateAt;
			private String fullName;
			private String accountName;
			
			@Transient
			private Long createMils;
			
			@Transient
			private Long updateMils;
			
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
			public Long getUpdatedMils() {
				if(this.updateAt == null) {
					return null;
				}else if(this.updateMils == null) {
					this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				}
				return this.updateMils; 
			}
			
			public void setChatting(String chatting) {
				this.chatting = Json.of(chatting);
			}

			public String getChatting() {
				if(this.chatting == null) {
					return "";
				}
				return this.chatting.asString();
			}
		}
	}
}
