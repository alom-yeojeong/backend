package com.arom.yeojung.repository;

import com.arom.yeojung.object.Friendship;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

  List<Friendship> findByUser1_UserIdOrUser2_UserId(Long user1Id, Long user2Id);
}