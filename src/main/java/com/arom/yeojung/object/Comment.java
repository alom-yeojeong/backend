package com.arom.yeojung.object;

import com.arom.yeojung.object.dto.CommentDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long commentId;

    //private User user;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "diaryid", nullable = false)
    @JoinColumn(name = "diaryId", nullable = false)
    private Diary diary;

    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentDto EntityToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setUserId(comment.getUser().getUserId());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getCreatedAt());
        return commentDto;
    }
}