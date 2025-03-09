package com.arom.yeojung.object.dto;

import com.arom.yeojung.object.BaseTimeEntity;
import com.arom.yeojung.object.DiaryStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DiaryDto{

    private Long userId;
    private String title;
    private Long viewCount;
    private Long commentCount;
    private Long likeCount;
    private DiaryStatus status; // DRAFT, PUBLIC, PRIVATE
    //private List<DiaryContentDto> contents; //DiaryContent 리스트
}
