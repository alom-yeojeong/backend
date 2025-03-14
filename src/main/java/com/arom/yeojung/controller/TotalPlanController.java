package com.arom.yeojung.controller;

import com.arom.yeojung.object.dto.TotalPlanRequestDTO;
import com.arom.yeojung.object.dto.TotalPlanResponseDTO;
import com.arom.yeojung.service.TotalPlanService;
import com.arom.yeojung.object.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/totalPlans")
@RequiredArgsConstructor
@Tag(
    name = "총계획 관리 API",
    description = "총계획 관리 API 제공"
)
public class TotalPlanController implements TotalPlanControllerDocs {
  private final TotalPlanService totalPlanService;

  // TotalPlan 생성
  @PostMapping
  public ResponseEntity<TotalPlanResponseDTO> createTotalPlan(
      @RequestBody @Valid TotalPlanRequestDTO requestDTO,
      @AuthenticationPrincipal User currentUser) {
    TotalPlanResponseDTO response = totalPlanService.createTotalPlan(requestDTO, currentUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 단일 TotalPlan 조회
  @GetMapping("/{totalPlanId}")
  public ResponseEntity<TotalPlanResponseDTO> getTotalPlan(@PathVariable Long totalPlanId) {
    TotalPlanResponseDTO response = totalPlanService.getTotalPlanById(totalPlanId);
    return ResponseEntity.ok(response);
  }

  // 모든 TotalPlan 조회
  @GetMapping
  public ResponseEntity<List<TotalPlanResponseDTO>> getAllTotalPlans() {
    List<TotalPlanResponseDTO> responses = totalPlanService.getAllTotalPlans();
    return ResponseEntity.ok(responses);
  }

  // TotalPlan 수정
  @PutMapping("/{totalPlanId}")
  public ResponseEntity<TotalPlanResponseDTO> updateTotalPlan(
      @PathVariable Long totalPlanId,
      @RequestBody @Valid TotalPlanRequestDTO requestDTO,
      @AuthenticationPrincipal User currentUser) {
    TotalPlanResponseDTO response = totalPlanService.updateTotalPlan(totalPlanId, requestDTO, currentUser);
    return ResponseEntity.ok(response);
  }

  // TotalPlan 삭제
  @DeleteMapping("/{totalPlanId}")
  public ResponseEntity<Void> deleteTotalPlan(
      @PathVariable Long totalPlanId,
      @AuthenticationPrincipal User currentUser) {
    totalPlanService.deleteTotalPlan(totalPlanId, currentUser);
    return ResponseEntity.noContent().build();
  }
}
