package com.arom.yeojung.repository;

import com.arom.yeojung.object.FriendRequest;
import com.arom.yeojung.object.constants.FriendStatus;
import com.arom.yeojung.object.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

  Optional<FriendRequest> findByFriendRequestId(Long friendRequestId);

  Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

  List<FriendRequest> findByReceiverAndStatus(User receiver, FriendStatus status);
}
