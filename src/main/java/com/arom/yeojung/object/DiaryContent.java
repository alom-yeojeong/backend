package com.arom.yeojung.object;

import com.arom.yeojung.object.dto.DiaryContentDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "diary_content")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryContent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_content_id")
    private Long diaryContentId;

    @ManyToOne
    @JoinColumn(name = "diaryId", nullable = false)
    private Diary diary;

    //현재 컨텐츠의 종류
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(columnDefinition = "TEXT")
    private String content;

    //File 엔티티와 연결
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "file_Id")
    private File file;
    //콘텐츠의 순서
    private Long sequence;

    //DiaryContent -> DiaryContentDto로 변환
    public static DiaryContentDto EntityToDto(DiaryContent diaryContent) {
        DiaryContentDto dto = new DiaryContentDto();
        dto.setDiaryId(diaryContent.getDiary().getDiaryId());
        dto.setContentType(diaryContent.getContentType());

        if(diaryContent.getContentType().equals(ContentType.TEXT) || diaryContent.getContentType().equals(ContentType.LINK)) {
            dto.setContent(diaryContent.getContent());
        } else {
            dto.setFileId(diaryContent.getFile().getFileId());
        }
        dto.setSequence(diaryContent.getSequence());
        return dto;
    }
}
