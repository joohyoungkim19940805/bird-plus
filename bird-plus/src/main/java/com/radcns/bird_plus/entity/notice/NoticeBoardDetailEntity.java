package com.radcns.bird_plus.entity.notice;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.r2dbc.postgresql.codec.Json;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
@Table("no_notice_board_detail")
@ToString
@Builder(toBuilder = true)
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class NoticeBoardDetailEntity {
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

    @Column("notice_board_id")
    private Long noticeBoardId;

    @Id
    @Column("id")
    private Long id;

    @Column("content")
    private Json content;

    @Transient
    private Long createMils;

    @Transient
    private Long updateMils;

    @Column("create_by")
    @CreatedBy
    private Long createBy;

    @Column("update_at")
    @LastModifiedDate
    private LocalDateTime updateAt;

    @Column("create_at")
    @CreatedDate
    private LocalDateTime createAt;

    @Column("update_by")
    @LastModifiedBy
    private Long updateBy;

    @Column("order_sort")
    private Long orderSort;

    @Column("room_id")
    private Long roomId;

    @Column("workspace_id")
    private Long workspaceId;

    @Column("empty_line_count")
    private Long emptyLineCount;
    

    public NoticeBoardDetailEntity withContent(String content) {
    	this.content = Json.of(content);
    	return this;
    }
    
    public void setContent(String content) {
        this.content = Json.of(content);
    }

    public String getContent() {
        if (this.content == null) {
            return "";
        }
        return this.content.asString();
    }
}