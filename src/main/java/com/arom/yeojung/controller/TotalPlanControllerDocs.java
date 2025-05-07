package com.arom.yeojung.controller;

import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.TotalPlanRequestDTO;
import com.arom.yeojung.object.dto.TotalPlanResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;

public interface TotalPlanControllerDocs {
  // 하나씩 입력할 수 있게 하는 그거 어떻게 하는거지
  @Operation(
      summary = "총 계획 생성",
      description = """
          **토큰 필요**
          
          **총 계획 생성**
          
          새로운 총 계획을 생성합니다.
          
          **입력 파라미터 값:**
          
          - **`String title`**: 계획의 이름
            _최대 20자_
          
          - **`String totalPlanDescription`**: 계획에 대한 상세 설명
            _최대 1000자_
            
          - **`LocalDate startDate`**: 여행 시작 날짜
          
          - **`LocalDate endDate`**: 여행 끝 날짜
          
          - **`Long totalBudget`**: 여행 계획 전체 예산
            _선택 사항_
          
          
         
          """
  )
  ResponseEntity<TotalPlanResponseDTO> createTotalPlan(
      @ModelAttribute @Valid TotalPlanRequestDTO requestDTO,
      @AuthenticationPrincipal User currentUser);

}
