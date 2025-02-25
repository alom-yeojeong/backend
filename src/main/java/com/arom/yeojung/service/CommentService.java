package com.arom.yeojung.service;

import com.arom.yeojung.object.Comment;
import com.arom.yeojung.object.Diary;
import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.CommentDto;
import com.arom.yeojung.repository.CommentRepository;
import com.arom.yeojung.repository.DiaryRepository;
import com.arom.yeojung.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private DiaryRepository diaryRepository;
    private UserRepository userRepository;

    //댓글 생성
    public CommentDto createComment(CommentDto commentDto) {
        User user = userRepository.findById(commentDto.getUserId())
                .orElseThrow(() -> new RuntimeException("not found user"));
        Diary diary = diaryRepository.findById(commentDto.getDiaryId())
                .orElseThrow(() -> new RuntimeException("not found diary"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setDiary(diary);
        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        //다이어리 불러오기 성공(사용자랑 다이어리 find 성공)했을 때만 증가
        //수정이 필요할 수 있음 (가능한지 실행해보아야 함)
        diary.setCommentCount(diary.getCommentCount() + 1);
        diaryRepository.save(diary);

        return comment.EntityToDto(comment);
    }

    //댓글 조회 (작성자로)
//사용자 예외 처리로 수정 필요
    public List<CommentDto> getCommentByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("not found user"));

        //하나의 유저에 대한 0개 이상의 댓글
        List<Comment> comments = commentRepository.findByUserId(user);
        if (comments.isEmpty()) {
            throw new RuntimeException("not found comment");
        }
        List<CommentDto> commentDtos = comments.stream().map(Comment::EntityToDto).collect(Collectors.toList());

        return commentDtos;
    }

    //댓글 조회 (다이어리로)
//다이어리 예외 처리로 수정 필요
    public List<CommentDto> getCommentByDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("not found diary"));

        //하나의 다이어리에 대한 0개 이상의 댓글
        List<Comment> comments = commentRepository.findByDiaryId(diary);
        if (comments.isEmpty()) {
            throw new RuntimeException("not found comment");
        }
        List<CommentDto> commentDtos = comments.stream().map(Comment::EntityToDto).collect(Collectors.toList());

        return commentDtos;
    }

    //댓글 수정
//댓글 예외 처리로 수정 필요
    public CommentDto updateComment(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getCommentId())
                .orElseThrow(() -> new RuntimeException("not found comment"));
        comment.setContent(commentDto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        return comment.EntityToDto(comment);
    }

    //댓글 삭제
//댓글 예외 처리로 수정 필요
    public String deleteComment(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getCommentId())
                .orElseThrow(() -> new RuntimeException("not found comment"));
        Diary diary = diaryRepository.findById(commentDto.getDiaryId())
                .orElseThrow(() -> new RuntimeException("not found diary"));

        commentRepository.delete(comment);

        //댓글 불러오기 성공(댓글 find 성공)했을 때만 감소
        //수정이 필요할 수 있음 (가능한지 실행해보아야 함)
        diary.setCommentCount(diary.getCommentCount() + 1);
        diaryRepository.save(diary);

        return "Comment deleted";
    }
}