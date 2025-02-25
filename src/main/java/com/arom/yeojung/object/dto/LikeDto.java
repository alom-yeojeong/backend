package com.arom.yeojung.object.dto;

import com.arom.yeojung.object.Diary;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LikeDto {
    private Long LikeId;
    private Long userId;
    private Long diaryId;
    private LocalDateTime createdAt;

}
