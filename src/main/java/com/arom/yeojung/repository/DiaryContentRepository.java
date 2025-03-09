package com.arom.yeojung.repository;

import com.arom.yeojung.object.DiaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryContentRepository extends JpaRepository<DiaryContent, Long> {
    public List<DiaryContent> findByDiary_DiaryId(Long diaryId);
}
