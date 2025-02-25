package com.arom.yeojung.controller;

import com.arom.yeojung.object.dto.CommentDto;
import com.arom.yeojung.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //댓글 생성
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.createComment(commentDto));
    }

    //댓글 조회 (작성자로)
    @GetMapping("/{userId}")
    public ResponseEntity<List<CommentDto>> getCommentsByUser(@PathVariable Long userId) {
        List<CommentDto> comments = commentService.getCommentByUser(userId);
        return ResponseEntity.ok(comments);
    }

    //댓글 조회 (다이어리로)
    @GetMapping("/{diaryId}")
    public ResponseEntity<List<CommentDto>> getCommentsByDiary(@PathVariable Long diaryId) {
        List<CommentDto> comments = commentService.getCommentByDiary(diaryId);
        return ResponseEntity.ok(comments);
    }

    //댓글 수정
    @PutMapping
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.updateComment(commentDto));
    }

    //댓글 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteComment(@RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.deleteComment(commentDto));
    }
}