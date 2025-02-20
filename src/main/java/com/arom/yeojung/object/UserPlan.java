package com.arom.yeojung.object;


import com.arom.yeojung.object.constants.PlanRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPlan extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userPlanId;

  // 사용자 (하나의 User는 여러 Plan에 참여 가능)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // 여행 계획 (하나의 Plan은 여러 User와 연결됨)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "totalPlan_id", nullable = false)
  private TotalPlan totalPlan;

  // 역할 (예: "리더", "참여자")
  @Enumerated(EnumType.STRING)
  private PlanRole role;
}
