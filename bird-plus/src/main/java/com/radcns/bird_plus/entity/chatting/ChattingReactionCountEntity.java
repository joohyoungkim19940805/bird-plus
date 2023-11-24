package com.radcns.bird_plus.entity.chatting;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.radcns.bird_plus.entity.chatting.ChattingReactionCountEntity.ChattingReactionCountDomain.ChattingReactionCountResponse;
import com.radcns.bird_plus.entity.emoticon.constant.EmoticonType;
@Table("ch_chatting_reaction_count")
@ToString
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@With
public class ChattingReactionCountEntity {
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
    
    @Column("reaction_id")
    private Long reactionId;

    @Column("emoticon_id")
    private Long emoticonId;

    @Column("chatting_id")
    private Long chattingId;

    @Column("account_id")
    private Long accountId;

    @Column("create_at")
    @CreatedDate
    private LocalDateTime createAt;

    @Column("update_at")
    @LastModifiedDate
    private LocalDateTime updateAt;

    @Transient
    private Long createMils;

    @Transient
    private Long updateMils;

    public static class ChattingReactionCountDomain{
    	@Getter
    	@Setter
    	@Builder(toBuilder = true)
    	public static class ChattingReactionCountResponse{
    		private String fullName;
    		private Long accountId;
    	}
    }

}