package com.arom.yeojung.object;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SubPlan extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long subPlanId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "dailyPlanId", nullable = false)
  private DailyPlan dailyPlan;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "locationId", nullable = true)
  private Location location;

  private String subPlanTitle;

  private String subPlanDescription;

  private LocalTime subPlanTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "createdBy", nullable = false, updatable = false)
  private User createdBy;

  @ManyToMany
  @JoinTable(
      name = "sub_plan_updaters",
      joinColumns = @JoinColumn(name = "sub_plan_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private Set<User> updaters = new HashSet<>();

}
