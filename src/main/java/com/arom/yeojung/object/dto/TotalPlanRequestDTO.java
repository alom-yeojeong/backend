package com.arom.yeojung.object.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalPlanRequestDTO {

  @NotNull
  private String title;

  private String totalPlanDescription;

  @NotNull
  private LocalDate startDate;

  @NotNull
  private LocalDate endDate;

  private Long totalBudget;

  private int travelDuration;

  // TODO: 여행지 받아야 함
  // TODO: 사진 받아야 함
}
