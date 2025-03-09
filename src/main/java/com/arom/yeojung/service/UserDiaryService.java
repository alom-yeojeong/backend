package com.arom.yeojung.service;

import com.arom.yeojung.object.*;
import com.arom.yeojung.object.dto.UserDiaryDto;
import com.arom.yeojung.repository.DiaryRepository;
import com.arom.yeojung.repository.UserDiaryRepository;
import com.arom.yeojung.repository.UserRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDiaryService extends BaseTimeEntity {

    private final UserDiaryRepository userDiaryRepository;
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    //특정 사용자의 모든 다이어리 조회
    public List<UserDiaryDto> getUserDiaryByUserId(Long userId) {
        List<UserDiary> userDiaries = userDiaryRepository.findAllByUser_UserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USERDIAYR_NOT_FOUND));
        return userDiaries.stream().map(UserDiary::EntityToDto).collect(Collectors.toList());
    }

    //특정 다이어리에 속한 사용자 목록 조회
    public List<UserDiaryDto> getUserDiaryByDiaryId(Long diaryId) {
        List<UserDiary> userDiaries = userDiaryRepository.findAllByDiary_DiaryId(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.USERDIAYR_NOT_FOUND));
        return userDiaries.stream().map(UserDiary::EntityToDto).collect(Collectors.toList());
    }

    //사용자가 다이어리에 참여
    public String addUerToDiary(Long userId, Long diaryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        UserDiary userDiary = new UserDiary();
        userDiary.setUser(user);
        userDiary.setDiary(diary);

        userDiaryRepository.save(userDiary);
        return "add User To Diary Success";
    }

    //사용자가 다이어리에서 나가기
    public String removeUerFromDiary(Long userId, Long diaryId, User currentUser) {
        UserDiary userDiary = userDiaryRepository.findByUser_UserIdAndDiary_DiaryId(userId, diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.USERDIAYR_NOT_FOUND));

        if(!userRepository.findByUserId(userId).equals(currentUser)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        userDiaryRepository.delete(userDiary);
        return "remove User From Diary Success";
    }
}
