package com.radcns.bird_plus.entity.chatting;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Table("ch_chatting_scrap")
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
@Setter
public class ChattingScrapEntity {
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

    @Column("chating_id")
    private Long chatingId;

    @Column("account_id")
    private Long accountId;

    @Column("update_at")
    @LastModifiedDate
    private LocalDateTime updateAt;

    @Column("id")
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
}