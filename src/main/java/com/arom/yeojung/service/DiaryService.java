package com.arom.yeojung.service;

import com.arom.yeojung.object.*;
import com.arom.yeojung.object.dto.DiaryDto;
import com.arom.yeojung.repository.*;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final DiaryContentRepository diaryContentRepository;
    private final FileRepository fileRepository;
    private final UserDiaryRepository userDiaryRepository;
    private final FileS3UploadService fileS3UploadService;

    //다이어리 생성
    @Transactional
    public DiaryDto createDiary(DiaryDto diaryDto) {
        //dto의 uerId를 통해 UserRepository에서 사용자 검색
        User user = userRepository.findById(diaryDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Diary diary = new Diary();
        diary.setUser(user);
        diary.setTitle(diaryDto.getTitle());
        diary.setViewCount(0L);
        diary.setCommentCount(0L);  //댓글이라 좋아요 수 추가
        diary.setLikeCount(0L);     //
        diary.setStatus(diaryDto.getStatus());

        //썸네일은 기본적으로 없는거로 설정
        diary.setThumbnailFile(fileRepository.findAll().getFirst());

        //UserDiary 자동 생성 (사용자와 다이어리 연결)
        UserDiary userDiary = new UserDiary();
        userDiary.setUser(user);
        userDiary.setDiary(diary);

        userDiaryRepository.save(userDiary);
        //다이어리 저장
        diaryRepository.save(diary);
        //DiaryContent 저장
 /*       List<DiaryContent> contents = diaryDto.getContents()
                .stream().map(contentDto -> {
                    DiaryContent diaryContent = new DiaryContent();

                    diaryContent.setDiary(diary);
                    diaryContent.setContentType(contentDto.getContentType());

                    //컨텐츠 타입이 텍스트나 링크라면 그냥 컨텐츠를 저장하고 이미지나 비디오라면 파일을 찾아서 저장
                    if(contentDto.getContentType() == ContentType.TEXT || contentDto.getContentType() == ContentType.LINK) {
                        diaryContent.setContent(contentDto.getContent());
                    } else if(contentDto.getContentType() == ContentType.IMAGE || contentDto.getContentType() == ContentType.VIDEO){
                        //파일아이디로 파일을 찾아서 파일을 저장
                        diaryContent.setFile(fileRepository.findById(contentDto.getFileId())
                                .orElseThrow(() -> new RuntimeException("not found file")));
                    }
                    //Content의 순서 저장
                    diaryContent.setSequence(contentDto.getSequence());

                    return diaryContentRepository.save(diaryContent);
                }).collect(Collectors.toList());

        //diary에 컨텐츠들 저장
        diary.setContents(contents);*/
        return Diary.EntityToDto(diary);
    }

    //다이어리 조회
    public DiaryDto getDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        return Diary.EntityToDto(diary);
    }

    //전체 다이어리 조회
    public List<DiaryDto> getAllDiary() {

        return diaryRepository.findAll().stream().map(Diary::EntityToDto).collect(Collectors.toList());
    }

    //다이어리 수정
    @Transactional
    public DiaryDto updateDiary(Long diaryId, DiaryDto diaryDto, User currentUser) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        validateAuthorization(diary, currentUser);

        diary.setTitle(diaryDto.getTitle());
        diary.setStatus(diaryDto.getStatus());

        return Diary.EntityToDto(diary);
    }

    //다이어리 제목 수정
    @Transactional
    public void updateDiaryTitle(Long diaryId, String title, User currentUser) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        validateAuthorization(diary, currentUser);

        diary.setTitle(title);

        diaryRepository.save(diary);
    }

    //다이어리 상태 변경
    @Transactional
    public void updateDiaryStatus(Long diaryId, DiaryStatus diaryStatus, User currentUser) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        validateAuthorization(diary, currentUser);

        diary.setStatus(diaryStatus);

        diaryRepository.save(diary);
    }

    //다이어리 썸네일 수정(새로운 사진으로 변경)
    @Transactional
    public File updateDiaryThumbnailNew(Long diaryId, MultipartFile file, User currentUser) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        validateAuthorization(diary, currentUser);

        File ThumbnailFile = fileS3UploadService.uploadAndSaveFile(file);
        diary.setThumbnailFile(ThumbnailFile);
        return ThumbnailFile;
    }

    //다이어리 썸네일 수정(원래 있던 사진으로 변경)
    @Transactional
    public File updateDiaryThumbnail(Long diaryId, Long fileId, User currentUser) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        File ThumbnailFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));

        validateAuthorization(diary, currentUser);

        diary.setThumbnailFile(ThumbnailFile);
        return ThumbnailFile;
    }

    //다이어리 삭제
    public void deleteDiary(Long diaryId, User currentUser) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        validateAuthorization(diary, currentUser);

        //다이어리 컨텐츠들도 모두 삭제
        diaryRepository.delete(diary);
    }
    // 권한 검증 로직 (사용자가 작성한 다이어리인지 확인)
    private void validateAuthorization(Diary diary, User currentUser) {
        if (!diary.getUser().equals(currentUser)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }
}