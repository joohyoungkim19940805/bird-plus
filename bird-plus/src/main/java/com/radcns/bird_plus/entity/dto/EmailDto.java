package com.radcns.bird_plus.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailDto {
   private String to;
   private String subject;
   private String content;
   private boolean isMultipart;
   private boolean isHtml;
}
