package com.arom.yeojung.object.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long commentId;
    private Long userId;
    private Long diaryId;
    private String content;
}