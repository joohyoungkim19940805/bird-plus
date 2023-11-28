package com.radcns.bird_plus.entity.chatting;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.radcns.bird_plus.entity.chatting.ChattingReactionCountEntity.ChattingReactionCountDomain.ChattingReactionCountResponse;
import com.radcns.bird_plus.entity.emoticon.constant.EmoticonType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Table("ch_chatting_reaction")
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
@Setter
public class ChattingReactionEntity {
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
        this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
        this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public Long getCreateMils() {
        if (this.createAt == null) {
            return null;
        } else if (this.createMils == null) {
            this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        return this.createMils;
    }

    public Long getUpdateMils() {
        if (this.updateAt == null) {
            return null;
        } else if (this.updateMils == null) {
            this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        return this.updateMils;
    }

    @Column("create_by")
    @CreatedBy
    private Long createBy;

    @Column("chatting_id")
    private Long chattingId;

    @Column("update_at")
    @LastModifiedDate
    private LocalDateTime updateAt;

    @Column("id")
    @Id
    private Long id;

    @Column("create_at")
    @CreatedDate
    private LocalDateTime createAt;

    @Column("update_by")
    @LastModifiedBy
    private Long updateBy;

    @Transient
    private Long createMils;

    @Transient
    private Long updateMils;

    @Column("room_id")
    private Long roomId;

    @Column("workspace_id")
    private Long workspaceId;

    @Column("emoticon_id")
    private Long emoticonId;
    
    public static class ChattingReactionDomain{
    	@Getter
    	@Setter
    	public static class ChattingReactionRequest{
    		private String emoticon;
    		private String description;
    		private EmoticonType emoticonType;
    		private String groupTitle;
    		private String subgroupTitle;
    		private Long workspaceId;
    		private Long roomId;
    		private Long chattingId;
    	}
    	@Getter
    	@Setter
    	@Builder(toBuilder = true)
    	public static class ChattingReactionResponse{
    		private String emoticon;
    		private EmoticonType emoticonType;
    		private Long workspaceId;
    		private Long roomId;
    		private Long chattingId;
    		private Long reactionId;
    		private Long count;
    		private String groupTitle;
    		private String subgroupTitle;
    		private List<ChattingReactionCountResponse> reactionList;
    	}
    }
}