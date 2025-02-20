package com.arom.yeojung.object;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TotalPlan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long totalPlanId;

  private String title;

  private LocalDate startDate;

  private LocalDate endDate;

  private Long totalBudget;

  private String totalPlanDescription;

  private int travelDuration;

  @OneToMany(mappedBy = "totalPlan", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserPlan> members = new ArrayList<>();

}
