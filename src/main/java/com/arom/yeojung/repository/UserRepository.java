package com.arom.yeojung.repository;

import com.arom.yeojung.object.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
