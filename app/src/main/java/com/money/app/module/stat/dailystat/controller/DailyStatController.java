package com.money.app.module.stat.dailystat.controller;


import com.money.app.security.CurrentUser;
import com.money.app.module.stat.dailystat.dto.DailySumStatListResponseDto;
import com.money.app.module.stat.dailystat.dto.DailySummaryStatDto;
import com.money.app.module.stat.dailystat.service.DailyStatService;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/daily")
public class DailyStatController {
    private final DailyStatService dailyStatService;

    public DailyStatController(DailyStatService dailyStatService) {
        this.dailyStatService = dailyStatService;
    }

    @GetMapping("/abc/{statDate}")
    public ResponseEntity<ApiResponse<AbcStatResponseDto>> getDailyAbc(
            @PathVariable LocalDate statDate,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        AbcStatResponseDto abc = dailyStatService.getDailyAbc(userId, statDate);
        return ApiResponseUtil.success(HttpStatus.OK, "일일 총 ABC 합산 조회 성공", abc);
    }

    @GetMapping("/sum/{statDate}")
    public ResponseEntity<ApiResponse<SumStatResponseDto>> getDailySum(
            @PathVariable LocalDate statDate,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        SumStatResponseDto sum = dailyStatService.getDailySum(userId, statDate);
        return ApiResponseUtil.success(HttpStatus.OK, "일일 총 수입 지출 합산 조회 성공", sum);
    }

    @GetMapping("/category/{statDate}")
    public ResponseEntity<ApiResponse<CategoryStatResponseDto>> getDailyCategory(
            @PathVariable LocalDate statDate,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        CategoryStatResponseDto category = dailyStatService.getDailyCategory(userId, statDate);
        return ApiResponseUtil.success(HttpStatus.OK, "일일 카테고리별 금액 조회 성공", category);
    }

    @GetMapping("/cate-abc/{statDate}")
    public ResponseEntity<ApiResponse<CateAbcResoponseDto>> getDailyCateAbc(
            @PathVariable LocalDate statDate,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        CateAbcResoponseDto category = dailyStatService.getDailyCateAbc(userId, statDate);
        return ApiResponseUtil.success(HttpStatus.OK, "일일 카테고리+ABC별 금액 조회 성공", category);
    }

    @GetMapping("/sum-list/{statDate}")
    public ResponseEntity<ApiResponse<List<DailySumStatListResponseDto>>> getDailySumList(
            @PathVariable LocalDate statDate,
            @AuthenticationPrincipal CurrentUser currentUser) {
        String userId = currentUser.getUserId();
        List<DailySumStatListResponseDto> sumList = dailyStatService.getDailySumList(userId, statDate);
        return ApiResponseUtil.success(HttpStatus.OK, "일별(월~일) 총 수입 지출 합산 조회 성공", sumList);
    }

    @GetMapping("/summary/{statDate}")
    public ResponseEntity<ApiResponse<DailySummaryStatDto>> getDailySummary(
            @PathVariable LocalDate statDate,
            @AuthenticationPrincipal CurrentUser currentUser) {
        String userId = currentUser.getUserId();
        DailySummaryStatDto summary = dailyStatService.getDailySummary(userId, statDate);
        return ApiResponseUtil.success(HttpStatus.OK, "일일 Summary 조회 성공", summary);
    }
}
