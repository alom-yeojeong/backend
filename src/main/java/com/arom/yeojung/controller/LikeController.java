package com.arom.yeojung.controller;

import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.LikeDto;
import com.arom.yeojung.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    //좋아요 생성
    @PostMapping
    public ResponseEntity<LikeDto> createLike(@RequestBody LikeDto likeDto) {
        return ResponseEntity.ok(likeService.createLike(likeDto));
    }

    //좋아요 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteLike(@RequestBody LikeDto likeDto,
                                             @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(likeService.deleteLike(likeDto, currentUser));
    }
}
