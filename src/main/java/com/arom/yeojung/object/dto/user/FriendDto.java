package com.arom.yeojung.object.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FriendDto {

  private UserDto user1;

  private UserDto user2;

}
