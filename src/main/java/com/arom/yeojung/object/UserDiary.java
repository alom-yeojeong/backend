package com.arom.yeojung.object;

import com.arom.yeojung.object.dto.UserDiaryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_diary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDiary extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_diary_id")
    private Long userDiaryId;

    @ManyToOne
    @JoinColumn(name = "user_Id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "diary_Id", nullable = false)
    private Diary diary;

    public static UserDiaryDto EntityToDto(UserDiary userDiary) {
        UserDiaryDto dto = new UserDiaryDto();
        dto.setUserId(userDiary.getUser().getUserId());
        dto.setDiaryId(userDiary.getDiary().getDiaryId());
        return dto;
    }
}
