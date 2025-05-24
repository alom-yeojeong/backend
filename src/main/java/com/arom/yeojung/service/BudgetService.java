package com.arom.yeojung.service;

import com.arom.yeojung.object.Budget;
import com.arom.yeojung.object.BudgetType;
import com.arom.yeojung.object.SubPlan;
import com.arom.yeojung.object.dto.BudgetRequestDTO;
import com.arom.yeojung.object.dto.BudgetResponseDTO;
import com.arom.yeojung.repository.BudgetRepository;
import com.arom.yeojung.repository.SubPlanRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final SubPlanRepository subPlanRepository;

    // 생성
    @Transactional
    public BudgetResponseDTO createBudget(BudgetRequestDTO dto) {
        // 요청에 담긴 subPlanId를 통해 SubPlan 엔티티 조회
        log.info("예산 생성 요청 시작: budgetCategory: {}, budgetName: {}, budgetAmount: {}", dto.getBudgetCategory(), dto.getBudgetName(), dto.getBudgetAmount());
        SubPlan subPlan = subPlanRepository.findById(dto.getSubPlanId())
                .orElseThrow(() -> {
                    log.error("예산 생성 실패: subPlan 조회 실패: subPlanId: {}", dto.getSubPlanId());
                    return new CustomException(ErrorCode.SUB_PLAN_NOT_FOUND);
                });

        Budget budget = Budget.builder()
                .budgetCategory(dto.getBudgetCategory())
                .budgetName(dto.getBudgetName())
                .budgetAmount(dto.getBudgetAmount())
                .budgetType(dto.getBudgetType())
                .subPlan(subPlan)
                .build();

        Budget saved = budgetRepository.save(budget);
        log.info("예산 생성 요청 성공: id: {}", saved.getBudgetId());
        return mapToResponseDTO(saved);
    }

    // 단건 조회
    @Transactional
    public BudgetResponseDTO getBudget(Long id) {
        log.info("예산 조회 요청: budgetId: {}", id);
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("예산 조회 실패: budgetId: {}", id);
                    return new CustomException(ErrorCode.BUDGET_NOT_FOUND);
                });
        log.info("예산 조회 성공: budgetId: {}", id);
        return mapToResponseDTO(budget);
    }

    // 전체 조회
    @Transactional
    public List<BudgetResponseDTO> getAllBudgets() {
        log.info("전체 예산 조회 요청");
        List<BudgetResponseDTO> responseList = budgetRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        log.info("전체 예산 조회 성공: 조회 건수: {}", responseList.size());
        return responseList;
    }

    // budgetType에 따른 조회
    @Transactional
    public List<BudgetResponseDTO> getBudgetsByBudgetType(BudgetType budgetType){
        log.info("budgetType에 따른 예산 조회 요청: budgetType: {}", budgetType);
        List<BudgetResponseDTO> responseList = budgetRepository.findByBudgetType(budgetType).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        log.info("budgetType에 따른 예산 조회 성공: budgetType: {}, 조회 건수: {}", budgetType, responseList.size());
        return responseList;
    }

    //subPlanId에 따른 조회
    @Transactional
    public List<BudgetResponseDTO> getBudgetListsBySubPlanId(Long subPlanID) {
        log.info("subPlanId에 따른 예산 조회 요청: subPlanId: {}", subPlanID);
        List<Budget> budgetList = budgetRepository.findBySubPlan_SubPlanId(subPlanID);
        List<BudgetResponseDTO> responseList = budgetList.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        log.info("subPlanId에 따른 예산 조회 성공: subPlanId: {}, 조회 건수: {}",subPlanID, responseList.size());
        return responseList;
    }

    // 수정
    @Transactional
    public BudgetResponseDTO updateBudget(Long id, BudgetRequestDTO dto) {
        log.info("예산 수정 요청: budgetId: {}", id);
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("예산 수정 요청 실패: budgetId: {}, 예산 조회 실패", id);
                    return new CustomException(ErrorCode.BUDGET_NOT_FOUND);
                });

        budget.setBudgetCategory(dto.getBudgetCategory());
        budget.setBudgetName(dto.getBudgetName());
        budget.setBudgetAmount(dto.getBudgetAmount());
        budget.setBudgetType(dto.getBudgetType());

        // SubPlan이 변경되었을 경우
        if (!budget.getSubPlan().getSubPlanId().equals(dto.getSubPlanId())) {
            SubPlan subPlan = subPlanRepository.findById(dto.getSubPlanId())
                    .orElseThrow(() -> {
                        log.error("예산 수정 실패: subplan 조회 실패: subplanId: {}", dto.getSubPlanId());
                        return new CustomException(ErrorCode.SUB_PLAN_NOT_FOUND);
                    });
            budget.setSubPlan(subPlan);
        }
        Budget updated = budgetRepository.save(budget);
        log.info("예산 수정 성공: budgetId: {}", id);
        return mapToResponseDTO(updated);
    }

    // 삭제
    @Transactional
    public void deleteBudget(Long id) {
        log.info("예산 삭제 요청: budgetId: {}", id);
        if (!budgetRepository.existsById(id)) {
            log.error("예산 삭제 실패: budgetId: {}, 존재하지 않는 예산", id);
            throw new CustomException(ErrorCode.BUDGET_NOT_FOUND);
        }
        budgetRepository.deleteById(id);
        log.info("예산 삭제 성공");
    }

    // Budget 엔티티를 BudgetResponseDTO로 매핑
    private BudgetResponseDTO mapToResponseDTO(Budget budget) {
        return BudgetResponseDTO.builder()
                .budgetId(budget.getBudgetId())
                .budgetCategory(budget.getBudgetCategory())
                .budgetName(budget.getBudgetName())
                .budgetAmount(budget.getBudgetAmount())
                .budgetType(budget.getBudgetType())
                .subPlanId(budget.getSubPlan().getSubPlanId())
                .build();
    }
}
