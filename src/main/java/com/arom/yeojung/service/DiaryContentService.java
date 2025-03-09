package com.arom.yeojung.service;

import com.arom.yeojung.object.*;
import com.arom.yeojung.object.dto.DiaryContentDto;
import com.arom.yeojung.repository.DiaryContentRepository;
import com.arom.yeojung.repository.DiaryRepository;
import com.arom.yeojung.repository.FileRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryContentService extends BaseTimeEntity {

    private final DiaryContentRepository diaryContentRepository;
    private final DiaryRepository diaryRepository;
    private final FileS3UploadService fileS3UploadService;
    private final FileRepository fileRepository;

    //다이어리에 컨텐츠 추가(이미지나 비디오)
    @Transactional
    public DiaryContentDto addDiaryContentMedia(Long diaryId, MultipartFile file, DiaryContentDto contentDto, User currentUser) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        validateAuthorization(diary, currentUser);

        //새로운 파일 업로드
        File upLoadFile = fileS3UploadService.uploadAndSaveFile(file);

        //업로드한 파일은 포함하는 Content 객체 생성
        DiaryContent content = new DiaryContent();
        content.setDiary(diary);
        content.setContentType(contentDto.getContentType());
        content.setFile(upLoadFile);
        content.setSequence(contentDto.getSequence());

        //diary에 컨텐츠 추가
        diary.addContent(content);

        //다이어리를 저장 (컨텐츠들도 자동으로 함께 저장)
        diaryRepository.save(diary);
        return contentDto;
    }

    //다이어리에 컨텐츠 추가(텍스트나 링크)
    @Transactional
    public DiaryContentDto addDiaryContentText(Long diaryId, DiaryContentDto contentDto, User currentUser) {
        //예외처리 해야함
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        validateAuthorization(diary, currentUser);

        DiaryContent content = new DiaryContent();

        content.setDiary(diary);
        content.setContentType(contentDto.getContentType());
        content.setContent(contentDto.getContent());
        content.setSequence(contentDto.getSequence());

        //diary에 컨텐츠 추가
        diary.addContent(content);

        //다이어리 저장
        diaryRepository.save(diary);
        return contentDto;
    }

    //다이어리 컨텐츠 수정(모든 변경 사항 처리)
    @Transactional
    public DiaryContentDto updateDiaryContent(Long diaryId, Long contentId, DiaryContentDto contentDto, User currentUser) {

        DiaryContent diaryContent = diaryContentRepository.findById(contentId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_CONTENT_NOT_FOUND));
        Diary diary = diaryRepository.findById(diaryId)
                        .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        validateAuthorization(diary, currentUser);

        // 텍스트 변경
        if (contentDto.getContent() != null) {
            diaryContent.setContent(contentDto.getContent());
        }

        // 이미지/비디오 변경
        if (contentDto.getFileId() != null) {
            diaryContent.setFile(fileRepository.findById(contentDto.getFileId())
                    .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND)));
        }

        //순서 변경 별도 -> 메소드에서 처리
        //if (contentDto.getSequence() != null) {
        //diaryContent.setSequence(contentDto.getSequence());
        //}

        //다이어리에 변경사항 저장
        Diary updatediary = diaryContent.getDiary();
        diaryRepository.save(updatediary);

        return DiaryContent.EntityToDto(diaryContent);
    }

    //다이어리 컨텐츠 중 텍스트 수정
    @Transactional
    public void updateContentText(Long diaryId, Long contentId, String text, User currentUser) {
        DiaryContent diaryContent = diaryContentRepository.findById(contentId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_CONTENT_NOT_FOUND));
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        validateAuthorization(diary, currentUser);

        diaryContent.setContent(text);
        Diary updatediary = diaryContent.getDiary();
        diaryRepository.save(updatediary);
    }

    //다이어리 컨텐츠 중 미디어 수정
    @Transactional
    public void updateContentMedia(Long diaryId, Long contentId, MultipartFile file, User currentUser) {
        DiaryContent diaryContent = diaryContentRepository.findById(contentId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_CONTENT_NOT_FOUND));
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
        validateAuthorization(diary, currentUser);

        File upLoadFile = fileS3UploadService.uploadAndSaveFile(file);
        diaryContent.setFile(upLoadFile);
        Diary updatediary = diaryContent.getDiary();
        diaryRepository.save(updatediary);
    }

    //컨텐츠 순서 변경
    @Transactional
    public void updateContentSequence(Long diaryId, Long contentId, Long newSequence, User currentUser) {
        List<DiaryContent> contents = diaryContentRepository.findByDiary_DiaryId(diaryId);
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
        validateAuthorization(diary, currentUser);

        DiaryContent targetContent = contents.stream()
                .filter(c -> c.getDiaryContentId().equals(contentId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_CONTENT_NOT_FOUND));

        Long oldSequence = targetContent.getSequence();

        //순서 뒤로 밀기 (ex: 6번 → 2번)
        if (oldSequence > newSequence) {
            for (DiaryContent content : contents) {
                if (content.getSequence() >= newSequence && content.getSequence() < oldSequence) {
                    content.setSequence(content.getSequence() + 1);
                }
            }
        }
        //순서 앞으로 당기기 (ex: 2번 → 6번)
        else if (oldSequence < newSequence) {
            for (DiaryContent content : contents) {
                if (content.getSequence() > oldSequence && content.getSequence() <= newSequence) {
                    content.setSequence(content.getSequence() - 1);
                }
            }
        }
        //이동할 컨텐츠의 새로운 순서 설정
        targetContent.setSequence(newSequence);

        //변경된 순서를 DB에 반영
        diaryContentRepository.saveAll(contents);
    }


    //다이어리 컨텐츠 조회
    public DiaryContentDto getDiaryContent(Long diaryId, Long contentId, User currentUser) {
         DiaryContent diaryContent = diaryContentRepository.findById(contentId)
                 .orElseThrow(() -> new CustomException(ErrorCode.DIARY_CONTENT_NOT_FOUND));
         Diary diary = diaryRepository.findById(diaryId)
                 .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
         validateAuthorization(diary, currentUser);

         //컨텐츠의 다이어리 아이디가 전달 받은 아이디랑 같을때만 실행
         if(diaryContent.getDiary().getDiaryId().equals(diaryId)) {
             return DiaryContent.EntityToDto(diaryContent);
         }
         return null;
    }

    //다이어리 컨텐츠 모두 조회
    public List<DiaryContentDto> getAllDiaryContents(Long diaryId) {
        List<DiaryContentDto> contentDtos = diaryContentRepository.findByDiary_DiaryId(diaryId)
                .stream().map(DiaryContent::EntityToDto).collect(Collectors.toList());
        return contentDtos;
    }

    //다이어리 컨텐츠 삭제
    @Transactional
    public void deleteDiaryContent(Long diaryId, Long contentId, User currentUser) {
        DiaryContent diaryContent = diaryContentRepository.findById(contentId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_CONTENT_NOT_FOUND));
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
        validateAuthorization(diary, currentUser);

        if(diaryContent.getDiary().getDiaryId().equals(diaryId)) {
            diaryContentRepository.delete(diaryContent);
        }
    }

    // 권한 검증 로직 (사용자가 작성한 다이어리인지 확인)
    private void validateAuthorization(Diary diary, User currentUser) {
        if (!diary.getUser().equals(currentUser)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }
}
