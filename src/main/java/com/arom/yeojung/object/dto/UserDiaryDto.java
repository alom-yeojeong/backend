package com.arom.yeojung.object.dto;

import com.arom.yeojung.object.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDiaryDto extends BaseTimeEntity {
    private Long userId;
    private Long diaryId;
}
