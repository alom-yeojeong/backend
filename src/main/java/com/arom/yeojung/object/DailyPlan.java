package com.arom.yeojung.object;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DailyPlan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dailyPlanId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "totalPlanId", nullable = false)
  private TotalPlan totalPlan;

  @Column(nullable = false)
  private LocalDate dailyPlanDate;

  private int tripDayNumber;

}
