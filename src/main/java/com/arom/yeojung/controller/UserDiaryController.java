package com.arom.yeojung.controller;

import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.UserDiaryDto;
import com.arom.yeojung.service.UserDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-diary")
@RequiredArgsConstructor
public class UserDiaryController {
    private final UserDiaryService userDiaryService;

    //특정 사용자의 모든 다이어리 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserDiaryDto>> getUserDiaries(@PathVariable Long userId) {
        return ResponseEntity.ok(userDiaryService.getUserDiaryByUserId(userId));
    }

    //특정 다이어리에 속한 모든 사용자 조회
    @GetMapping("/{diaryId}")
    public ResponseEntity<List<UserDiaryDto>> getUsersInDiary(@PathVariable Long diaryId) {
        return ResponseEntity.ok(userDiaryService.getUserDiaryByDiaryId(diaryId));
    }

    // 사용자가 다이어리에 참여
    @PostMapping
    public ResponseEntity<String> addUserToDiary(@RequestParam Long userId, @RequestParam Long diaryId) {
        return ResponseEntity.ok(userDiaryService.addUerToDiary(userId, diaryId));
    }

    //사용자가 다이어리에서 나가기
    @DeleteMapping()
    public ResponseEntity<String> removeUserFromDiary(@RequestParam Long userId,
                                                      @RequestParam Long diaryId,
                                                      @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userDiaryService.removeUerFromDiary(userId, diaryId, currentUser));
    }
}
