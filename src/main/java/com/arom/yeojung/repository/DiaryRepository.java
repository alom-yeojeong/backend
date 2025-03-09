package com.arom.yeojung.repository;

import com.arom.yeojung.object.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

}
