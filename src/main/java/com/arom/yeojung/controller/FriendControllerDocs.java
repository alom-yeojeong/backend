package com.arom.yeojung.controller;

import com.arom.yeojung.object.dto.user.CustomUserDetails;
import com.arom.yeojung.object.dto.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface FriendControllerDocs {

  @Operation(
      summary = "친구 요청 보내기",
      description = """
      ## 인증(JWT): **필수**

      ## 요청 파라미터 (RequestParam)
      - **`receiverId`**: 친구 요청을 보낼 상대방의 ID

      ## 반환값
      - 친구 요청이 성공적으로 전송된 경우 `200 OK` 응답

      ## 에러코드
      - **`ALREADY_REQUESTED_FRIENDSHIP`**: 이미 친구 요청을 보낸 상태입니다.
      - **`USER_NOT_FOUND`**: 사용자를 찾을 수 없습니다.
      """
  )
  ResponseEntity<Void> sendFriendRequest(CustomUserDetails userDetails, Long receiverId);

  @Operation(
      summary = "친구 요청 수락",
      description = """
      ## 인증(JWT): **필수**

      ## 요청 파라미터 (RequestParam)
      - **`senderId`**: 친구 요청을 보낸 사용자의 ID

      ## 반환값
      - 친구 요청이 수락되면 `200 OK` 응답

      ## 에러코드
      - **`USER_NOT_FOUND`**: 사용자를 찾을 수 없습니다.
      - **`REQUEST_NOT_FOUND`**: 해당 친구 요청을 찾을 수 없습니다.
      """
  )
  ResponseEntity<Void> acceptFriendRequest(CustomUserDetails userDetails, Long senderId);

  @Operation(
      summary = "친구 요청 거절",
      description = """
      ## 인증(JWT): **필수**

      ## 요청 파라미터 (RequestParam)
      - **`senderId`**: 친구 요청을 보낸 사용자의 ID

      ## 반환값
      - 친구 요청이 거절되면 `200 OK` 응답

      ## 에러코드
      - **`USER_NOT_FOUND`**: 사용자를 찾을 수 없습니다.
      - **`REQUEST_NOT_FOUND`**: 해당 친구 요청을 찾을 수 없습니다.
      """
  )
  ResponseEntity<Void> rejectFriendRequest(CustomUserDetails userDetails, Long senderId);

  @Operation(
      summary = "친구 요청 받은 사용자 리스트 조회",
      description = """
      ## 인증(JWT): **필수**

      ## 반환값 (List<UserDto>)
      - 친구 요청을 받은 사용자 리스트를 반환

      ## 에러코드
      - **`USER_NOT_FOUND`**: 사용자를 찾을 수 없습니다.
      """
  )
  ResponseEntity<List<UserDto>> getFriendRequests(CustomUserDetails userDetails);

  @Operation(
      summary = "사용자의 친구 리스트 조회",
      description = """
      ## 인증(JWT): **필수**

      ## 반환값 (List<UserDto>)
      - 사용자의 친구 리스트를 반환

      ## 에러코드
      - **`USER_NOT_FOUND`**: 사용자를 찾을 수 없습니다.
      """
  )
  ResponseEntity<List<UserDto>> getFriendList(CustomUserDetails userDetails);
}
