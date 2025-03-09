package com.arom.yeojung.service;

import com.arom.yeojung.object.Comment;
import com.arom.yeojung.object.Diary;
import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.CommentDto;
import com.arom.yeojung.repository.CommentRepository;
import com.arom.yeojung.repository.DiaryRepository;
import com.arom.yeojung.repository.UserRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private DiaryRepository diaryRepository;
    private UserRepository userRepository;

    //댓글 생성
    @Transactional
    public CommentDto createComment(CommentDto commentDto) {
        User user = userRepository.findById(commentDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Diary diary = diaryRepository.findById(commentDto.getDiaryId())
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setDiary(diary);
        comment.setContent(commentDto.getContent());

        try {
            commentRepository.save(comment);
            diary.setCommentCount(diary.getCommentCount() + 1);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.SAVE_FAILED);
        }

        return Comment.EntityToDto(comment);
    }

    //댓글 조회 (작성자로)
    public List<CommentDto> getCommentByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //하나의 유저에 대한 0개 이상의 댓글
        List<Comment> comments = commentRepository.findByUser_UserId(user);
        if (comments.isEmpty()) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return comments.stream().map(Comment::EntityToDto).collect(Collectors.toList());
    }

    //댓글 조회 (다이어리로)
    public List<CommentDto> getCommentByDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        //하나의 다이어리에 대한 0개 이상의 댓글
        List<Comment> comments = commentRepository.findByDiary_DiaryId(diary);
        if (comments.isEmpty()) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return comments.stream().map(Comment::EntityToDto).collect(Collectors.toList());
    }

    //댓글 수정
    @Transactional
    public CommentDto updateComment(CommentDto commentDto, User currentUser) {
        Comment comment = commentRepository.findById(commentDto.getCommentId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        validateAuthorization(comment, currentUser);

        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);

        return Comment.EntityToDto(comment);
    }

    //댓글 삭제
    @Transactional
    public String deleteComment(CommentDto commentDto, User currentUser) {
        Comment comment = commentRepository.findById(commentDto.getCommentId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        Diary diary = diaryRepository.findById(commentDto.getDiaryId())
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        validateAuthorization(comment, currentUser);

        try {
            commentRepository.delete(comment);
            diary.setCommentCount(diary.getCommentCount() - 1);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.SAVE_FAILED);
        }

        return "Comment deleted";
    }

    // 권한 검증 로직 (사용자가 작성한 댓글인지 확인)
    private void validateAuthorization(Comment comment, User currentUser) {
        if (!comment.getUser().equals(currentUser)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }
}