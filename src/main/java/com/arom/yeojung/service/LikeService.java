package com.arom.yeojung.service;

import com.arom.yeojung.object.BaseTimeEntity;
import com.arom.yeojung.object.Diary;
import com.arom.yeojung.object.Like;
import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.LikeDto;
import com.arom.yeojung.repository.DiaryRepository;
import com.arom.yeojung.repository.LikeRepository;
import com.arom.yeojung.repository.UserRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LikeService extends BaseTimeEntity {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    //좋아요 생성
    public LikeDto createLike(LikeDto likeDto) {
        User user = userRepository.findById(likeDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Diary diary = diaryRepository.findById(likeDto.getDiaryId())
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        Like like = new Like();
        like.setUser(user);
        like.setDiary(diary);
        likeRepository.save(like);
        return likeDto;
    }

    //좋아요 삭제
    public String deleteLike(LikeDto likeDto, User currentUser) {
        Like like = likeRepository.findByUser_UserIdAndDiary_DiaryId(likeDto.getUserId(), likeDto.getDiaryId())
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND));

        validateAuthorization(like, currentUser);

        likeRepository.delete(like);
        return "success";
    }

    // 권한 검증 로직 (사용자가 작성한 좋아요인지 확인)
    private void validateAuthorization(Like like, User currentUser) {
        if (!like.getUser().equals(currentUser)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }
}