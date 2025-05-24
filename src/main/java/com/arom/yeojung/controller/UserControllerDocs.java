package com.arom.yeojung.controller;

import com.arom.yeojung.object.dto.user.CustomUserDetails;
import com.arom.yeojung.object.dto.user.ProfileImageRequest;
import com.arom.yeojung.object.dto.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

public interface UserControllerDocs {

  @Operation(
      summary = "회원 정보 조회",
      description = """
      ## 인증(JWT): **필수**

      ## 요청 파라미터 (Path Variable)
      - **`userId`**: 조회할 회원의 ID

      ## 반환값 (UserDto)
      - **`username`**: 사용자 이름
      - **`nickname`**: 닉네임
      - **`profileImageUrl`**: 프로필 이미지 URL

      ## 에러코드
      - **`USER_NOT_FOUND`**: 회원을 찾을 수 없습니다.
      """
  )
  ResponseEntity<UserDto> getMemberInfo(Long userId);

  @Operation(
      summary = "닉네임 변경",
      description = """
      ## 인증(JWT): **필수**

      ## 요청 파라미터 (RequestParam)
      - **`nickname`**: 변경할 닉네임

      ## 반환값 (UserDto)
      - **`username`**: 사용자 이름
      - **`nickname`**: 닉네임
      - **`profileImageUrl`**: 프로필 이미지 URL

      ## 에러코드
      - **`DUPLICATED_NICKNAME`**: 이미 사용 중인 닉네임입니다.
      """
  )
  ResponseEntity<UserDto> updateNickname(CustomUserDetails userDetails, String nickname);

  @Operation(
      summary = "닉네임 중복 체크",
      description = """
      ## 인증(JWT): **불필요**

      ## 요청 파라미터 (RequestParam)
      - **`nickname`**: 중복 확인할 닉네임

      ## 반환값 (Boolean)
      - **`true`**: 사용 가능한 닉네임
      - **`false`**: 이미 사용 중인 닉네임
      """
  )
  ResponseEntity<Boolean> duplicateNickname(String nickname);

  @Operation(
      summary = "프로필 사진 등록",
      description = """
      ## 인증(JWT): **필수**

      ## 요청 파라미터 (ProfileImageRequest)
      - **`profileImage`**: 등록할 프로필 이미지 파일 (20MB 이하)

      ## 반환값 (UserDto)
      - **`username`**: 사용자 이름
      - **`nickname`**: 닉네임
      - **`profileImageUrl`**: 프로필 이미지 URL

      ## 에러코드
      - **`FILE_SIZE_EXCEED`**: 파일 크기가 20MB를 초과했습니다.
      """
  )
  ResponseEntity<UserDto> registerProfileImage(CustomUserDetails userDetails, ProfileImageRequest request);

  @Operation(
      summary = "프로필 사진 변경",
      description = """
      ## 인증(JWT): **필수**

      ## 요청 파라미터 (ProfileImageRequest)
      - **`profileImage`**: 변경할 프로필 이미지 파일 (20MB 이하)

      ## 반환값 (UserDto)
      - **`username`**: 사용자 이름
      - **`nickname`**: 닉네임
      - **`profileImageUrl`**: 프로필 이미지 URL

      ## 에러코드
      - **`FILE_SIZE_EXCEED`**: 파일 크기가 20MB를 초과했습니다.
      """
  )
  ResponseEntity<UserDto> updateProfile(CustomUserDetails userDetails, ProfileImageRequest request);
}
