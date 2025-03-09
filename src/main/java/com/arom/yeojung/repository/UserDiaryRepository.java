package com.arom.yeojung.repository;

import com.arom.yeojung.object.UserDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDiaryRepository extends JpaRepository<UserDiary, Long> {
    Optional<List<UserDiary>> findAllByUser_UserId(Long userId);
    Optional<List<UserDiary>> findAllByDiary_DiaryId(Long diaryId);
    void deleteByUser_UserIdAndDiary_DiaryId(Long userId, Long diaryId);
    Optional<UserDiary> findByUser_UserIdAndDiary_DiaryId(Long userId, Long diaryId);
}
