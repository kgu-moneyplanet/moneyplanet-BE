package com.money.app.module.stat.monthlystat.controller;


import com.money.app.module.stat.monthlystat.dto.MonthlySummaryStatDto;
import com.money.app.module.stat.monthlystat.service.MonthlyStatService;
import com.money.app.security.CurrentUser;
import com.money.app.module.stat.monthlystat.dto.MonthlySumStatListResponseDto;
import com.money.app.util.common.dto.AbcStatResponseDto;
import com.money.app.util.common.dto.CateAbcResoponseDto;
import com.money.app.util.common.dto.CategoryStatResponseDto;
import com.money.app.util.common.dto.SumStatResponseDto;
import com.money.app.util.response.ApiResponse;
import com.money.app.util.response.ApiResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/monthly")
public class MonthlyStatController {
    private final MonthlyStatService monthlyStatService;

    public MonthlyStatController(MonthlyStatService monthlyStatService) {
        this.monthlyStatService = monthlyStatService;
    }

    @GetMapping("/abc/{year}/{month}")
    public ResponseEntity<ApiResponse<AbcStatResponseDto>> getMonthlyAbc(
            @PathVariable int year,
            @PathVariable int month,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        AbcStatResponseDto abc = monthlyStatService.getMonthlyAbc(userId, year, month);
        return ApiResponseUtil.success(HttpStatus.OK, "월간 총 ABC 합산 조회 성공", abc);
    }

    @GetMapping("/sum/{year}/{month}")
    public ResponseEntity<ApiResponse<SumStatResponseDto>> getMonthlySum(
            @PathVariable int year,
            @PathVariable int month,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        SumStatResponseDto sum = monthlyStatService.getMonthlySum(userId, year, month);
        return ApiResponseUtil.success(HttpStatus.OK, "월간 총 수입 지출 합산 조회 성공", sum);
    }

    @GetMapping("/category/{year}/{month}")
    public ResponseEntity<ApiResponse<CategoryStatResponseDto>> getMonthlyCategory(
            @PathVariable int year,
            @PathVariable int month,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        CategoryStatResponseDto category = monthlyStatService.getMonthlyCategory(userId, year, month);
        return ApiResponseUtil.success(HttpStatus.OK, "월간 카테고리별 금액 조회 성공", category);
    }

    @GetMapping("/cate-abc/{year}/{month}")
    public ResponseEntity<ApiResponse<CateAbcResoponseDto>> getMonthlyCateAbc(
            @PathVariable int year,
            @PathVariable int month,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        CateAbcResoponseDto category = monthlyStatService.getMonthlyCateAbc(userId, year, month);
        return ApiResponseUtil.success(HttpStatus.OK, "주별 카테고리+ABC별 금액 조회 성공", category);
    }

    @GetMapping("/sum-list/{year}/{month}")
    public ResponseEntity<ApiResponse<List<MonthlySumStatListResponseDto>>> getMonthlySumList(
            @PathVariable int year,
            @PathVariable int month,
            @AuthenticationPrincipal CurrentUser currentUser) {
        String userId = currentUser.getUserId();
        List<MonthlySumStatListResponseDto> sumList = monthlyStatService.getMonthlySumList(userId, year, month);
        return ApiResponseUtil.success(HttpStatus.OK, "주별(월~일) 총 수입 지출 합산 조회 성공", sumList);
    }

    @GetMapping("/summary/{year}/{month}")
    public ResponseEntity<ApiResponse<MonthlySummaryStatDto>> getMonthlySummary(
            @PathVariable int year,
            @PathVariable int month,
            @AuthenticationPrincipal CurrentUser currentUser) {
        String userId = currentUser.getUserId();
        MonthlySummaryStatDto summary = monthlyStatService.getMonthlySummary(userId, year, month);
        return ApiResponseUtil.success(HttpStatus.OK, "주간 Summary + 해당주 월화수목금토일 수입지출 조회 성공", summary);
    }

}
