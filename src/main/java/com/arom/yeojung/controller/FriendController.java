package com.arom.yeojung.controller;

import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.user.CustomUserDetails;
import com.arom.yeojung.object.dto.user.UserDto;
import com.arom.yeojung.service.FriendService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@Tag(
    name = "친구 관리 API",
    description = "친구 관리 API 제공"
)
public class FriendController implements FriendControllerDocs{

  private final FriendService friendService;

  // 친구 요청 보내기
  @Override
  @PostMapping("/request")
  public ResponseEntity<Void> sendFriendRequest(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestParam Long receiverId) {
    Long userId = userDetails.getUserId();
    friendService.sendFriendRequest(userId, receiverId);
    return ResponseEntity.ok().build();
  }

  // 친구 요청 수락
  @Override
  @PostMapping("/accept")
  public ResponseEntity<Void> acceptFriendRequest(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestParam Long senderId) {
    User user = userDetails.getUser();
    friendService.acceptFriendRequest(user, senderId);
    return ResponseEntity.ok().build();
  }

  // 친구 요청 거절
  @Override
  @PostMapping("/reject")
  public ResponseEntity<Void> rejectFriendRequest(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestParam Long senderId) {
    User user = userDetails.getUser();
    friendService.rejectFriendRequest(user, senderId);
    return ResponseEntity.ok().build();
  }

  // 친구 요청 받은 사용자 리스트 조회
  @Override
  @GetMapping("/requests")
  public ResponseEntity<List<UserDto>> getFriendRequests(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    User user = userDetails.getUser();
    return ResponseEntity.ok(friendService.getFriendRequest(user));
  }

  // 사용자의 친구 리스트 조회
  @Override
  @GetMapping("/list")
  public ResponseEntity<List<UserDto>> getFriendList(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    User user = userDetails.getUser();
    return ResponseEntity.ok(friendService.getFriendList(user));
  }
}
