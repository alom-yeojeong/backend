package com.arom.yeojung.controller;

import com.arom.yeojung.object.DiaryStatus;
import com.arom.yeojung.object.File;
import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.DiaryContentDto;
import com.arom.yeojung.object.dto.DiaryDto;
import com.arom.yeojung.service.DiaryContentService;
import com.arom.yeojung.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;
    private final DiaryContentService diaryContentService;

    //다이어리 생성 Create
    @PostMapping
    public ResponseEntity<DiaryDto> createDiary(@RequestBody DiaryDto diaryDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(diaryService.createDiary(diaryDto));
    }

    //특정 다이어리 조회 Read
    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryDto> getDiary(@PathVariable Long diaryId) {
        return ResponseEntity.ok(diaryService.getDiary(diaryId));
    }

    //모든 다이어리 조회
    @GetMapping
    public ResponseEntity<List<DiaryDto>> getAllDiaries() {
        return ResponseEntity.ok(diaryService.getAllDiary());
    }

    //다이어리 수정 Update
    @PutMapping("/{diaryId}")
    public ResponseEntity<DiaryDto> updateDiary(@PathVariable Long diaryId, @RequestBody DiaryDto diaryDto,
                                                @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok((diaryService.updateDiary(diaryId, diaryDto, currentUser)));
    }

    //다이어리 제목 수정
    @PatchMapping("/{diaryId}/title")
    public ResponseEntity<Void> updateDiaryTitle(@PathVariable Long diaryId, @RequestParam("title") String title,
                                                 @AuthenticationPrincipal User currentUser) {
        diaryService.updateDiaryTitle(diaryId, title,currentUser);
        return ResponseEntity.ok().build();
    }

    //다이어리 공개/비공개 수정
    @PatchMapping("{diary}/public")
    public ResponseEntity<Void> updateDiaryStatus(@PathVariable Long diaryId, @RequestParam DiaryStatus diaryStatus,
                                                  @AuthenticationPrincipal User currentUser) {
        diaryService.updateDiaryStatus(diaryId, diaryStatus, currentUser);
        return ResponseEntity.ok().build();
    }

    //다이어리 썸네일 변경(새로운 사진으로 변경)
    @PutMapping("{diaryId}/thumbnail")
    public ResponseEntity<File> updateDiaryThumbnail(@PathVariable Long diaryId, @ModelAttribute MultipartFile file,
                                                     @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(diaryService.updateDiaryThumbnailNew(diaryId, file, currentUser));
    }

    //다이어리 썸네일 변경(원래 있던 사진으로 변경)
    @PutMapping("{diaryId}/thumbnail/{fileId}")
    public ResponseEntity<File> updateDiaryThumbnail(@PathVariable Long diaryId, @PathVariable Long fileId,
                                                     @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(diaryService.updateDiaryThumbnail(diaryId, fileId, currentUser));
    }

    //다이어리 삭제 Delete
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<String> deleteDiary(@PathVariable Long diaryId,
                                              @AuthenticationPrincipal User currentUser) {
        diaryService.deleteDiary(diaryId, currentUser);
        //해당 다이어리에 컨텐츠들 모두 삭제
        return ResponseEntity.ok("Diary deleted.");
    }

    //특정 다이어리에 diaryContent 추가(이미지나 비디오)
    @PostMapping("/{diaryId}/contents/text")
    public ResponseEntity<DiaryContentDto> addDiaryContentMedia(
            @PathVariable Long diaryId,
            @ModelAttribute MultipartFile file,
            @RequestBody DiaryContentDto diaryContentDto,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(diaryContentService.addDiaryContentMedia(diaryId, file, diaryContentDto, currentUser));
    }

    //특정 다이어리에 diaryContent 추가(텍스트나 링크)
    @PostMapping("/{diaryId}/contents/media")
    public ResponseEntity<DiaryContentDto> addDiaryContentText(
            @PathVariable Long diaryId,
            @RequestBody DiaryContentDto diaryContentDto,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(diaryContentService.addDiaryContentText(diaryId, diaryContentDto, currentUser));
    }

    //컨텐츠 텍스트 변경
    @PatchMapping("/{diaryId}/contents/{contentId}/text")
    public ResponseEntity<Void> updateContentText(
            @PathVariable Long diaryId,
            @PathVariable Long contentId,
            @RequestParam("text") String text,
            @AuthenticationPrincipal User currentUser) {
        diaryContentService.updateContentText(diaryId, contentId, text, currentUser);
        return ResponseEntity.ok().build();
    }

    //컨텐츠 미디어 변경
    @PatchMapping("/{diaryId}/contents/{contentId}/media")
    public ResponseEntity<Void> updateContentMedia(
            @PathVariable Long diaryId,
            @PathVariable Long contentId,
            @RequestParam("media") MultipartFile file,
            @AuthenticationPrincipal User currentUser) {
        diaryContentService.updateContentMedia(diaryId, contentId, file, currentUser);
        return ResponseEntity.ok().build();
    }

    //컨텐츠 순서 변경
    @PatchMapping("/{diaryId}/contents/{contentId}/sequence")
    public ResponseEntity<Void> updateContentSequence(
            @PathVariable Long diaryId,
            @PathVariable Long contentId,
            @RequestParam("sequence") Long newSequence,
            @AuthenticationPrincipal User currentUser
    ) {
        diaryContentService.updateContentSequence(diaryId, contentId, newSequence, currentUser);
        return ResponseEntity.ok().build();
    }

    //컨텐츠 일괄 수정
    @PutMapping("/{diaryId}/contents/{contentId}")
    public ResponseEntity<Void> updateDiaryContent(
            @PathVariable Long diaryId,
            @PathVariable Long contentId,
            @RequestBody DiaryContentDto diaryContentDto,
            @AuthenticationPrincipal User currentUser) {
        diaryContentService.updateDiaryContent(diaryId, contentId, diaryContentDto, currentUser);
        return ResponseEntity.ok().build();
    }

    //다이어리에서 특정 diaryContent 삭제
    @DeleteMapping("/{diaryId}/contents/{contentId}")
    public ResponseEntity<String> deleteDiaryContent(
            @PathVariable Long diaryId,
            @PathVariable Long contentId,
            @AuthenticationPrincipal User currentUser
    ) {
        diaryContentService.deleteDiaryContent(diaryId, contentId, currentUser);
        return ResponseEntity.ok("Content deleted successfully.");
    }
}
