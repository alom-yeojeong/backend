package com.arom.yeojung.object;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@ManyToOne
    //@JoinColumn(name = "user_id", nullable = false)
    //private User user;

    //@OneToOne
    //priavte DailyPlan dailyplan

    //제목
    private String title;

    //생성시각과 수정시각
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long CommentCount;

    //조회수
    private Long viewCount;

    //섬네일 경로
    private String thumbnailPath;

    //현재 다이어리의 상태
    //DRAFT(임시저쟝) PUBLIC(공개) PRIVATE(비공개)
    @Enumerated(EnumType.STRING)
    private DiaryStatus status;

    //컨테츠 들을 담은 리스트 순서 오름차순으로 정렬
    @OneToMany
    @OrderBy("sequence ASC")
    private List<DiaryContent> contents = new ArrayList<>();
}
