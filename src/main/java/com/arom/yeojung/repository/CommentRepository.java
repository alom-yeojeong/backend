package com.arom.yeojung.repository;

import com.arom.yeojung.object.Comment;
import com.arom.yeojung.object.Diary;
import com.arom.yeojung.object.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserId(User user);

    List<Comment> findByDiaryId(Diary diary);
}
