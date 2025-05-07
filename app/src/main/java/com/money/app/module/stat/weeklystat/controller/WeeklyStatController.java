package com.money.app.module.stat.weeklystat.controller;


import com.money.app.module.stat.weeklystat.dto.WeeklyLWTWAmountDto;
import com.money.app.module.stat.weeklystat.dto.WeeklySumStatListResponseDto;
import com.money.app.module.stat.weeklystat.dto.WeeklySummaryStatDto;
import com.money.app.module.stat.weeklystat.service.WeeklyStatService;
import com.money.app.security.CurrentUser;
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
@RequestMapping("/v1/weekly")
public class WeeklyStatController {
    private final WeeklyStatService weeklyStatService;

    public WeeklyStatController(WeeklyStatService weeklyStatService) {
        this.weeklyStatService = weeklyStatService;
    }

    @GetMapping("/abc/{year}/{weekNum}")
    public ResponseEntity<ApiResponse<AbcStatResponseDto>> getWeeklyAbc(
            @PathVariable int year,
            @PathVariable int weekNum,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        AbcStatResponseDto abc = weeklyStatService.getWeeklyAbc(userId, year, weekNum);
        return ApiResponseUtil.success(HttpStatus.OK, "주간 총 ABC 합산 조회 성공", abc);
    }

    @GetMapping("/sum/{year}/{weekNum}")
    public ResponseEntity<ApiResponse<SumStatResponseDto>> getWeeklySum(
            @PathVariable int year,
            @PathVariable int weekNum,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        SumStatResponseDto sum = weeklyStatService.getWeeklySum(userId, year, weekNum);
        return ApiResponseUtil.success(HttpStatus.OK, "주간 총 수입 지출 합산 조회 성공", sum);
    }

    @GetMapping("/category/{year}/{weekNum}")
    public ResponseEntity<ApiResponse<CategoryStatResponseDto>> getWeeklyCategory(
            @PathVariable int year,
            @PathVariable int weekNum,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        CategoryStatResponseDto category = weeklyStatService.getWeeklyCategory(userId, year, weekNum);
        return ApiResponseUtil.success(HttpStatus.OK, "주간 카테고리별 금액 조회 성공", category);
    }

    @GetMapping("/cate-abc/{year}/{weekNum}")
    public ResponseEntity<ApiResponse<CateAbcResoponseDto>> getWeeklyCateAbc(
            @PathVariable int year,
            @PathVariable int weekNum,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        CateAbcResoponseDto category = weeklyStatService.getWeeklyCateAbc(userId, year, weekNum);
        return ApiResponseUtil.success(HttpStatus.OK, "주별 카테고리+ABC별 금액 조회 성공", category);
    }

    @GetMapping("/sum-list/{year}/{weekNum}")
    public ResponseEntity<ApiResponse<List<WeeklySumStatListResponseDto>>> getWeekySumList(
            @PathVariable int year,
            @PathVariable int weekNum,
            @AuthenticationPrincipal CurrentUser currentUser) {
        String userId = currentUser.getUserId();
        List<WeeklySumStatListResponseDto> sumList = weeklyStatService.getWeeklySumList(userId, year, weekNum);
        return ApiResponseUtil.success(HttpStatus.OK, "주별(월~일) 총 수입 지출 합산 조회 성공", sumList);
    }

    @GetMapping("/summary/{year}/{weekNum}")
    public ResponseEntity<ApiResponse<WeeklySummaryStatDto>> getWeeklySummary(
            @PathVariable int year,
            @PathVariable int weekNum,
            @AuthenticationPrincipal CurrentUser currentUser) {
        String userId = currentUser.getUserId();
        WeeklySummaryStatDto summary = weeklyStatService.getWeeklySummary(userId, year, weekNum);
        return ApiResponseUtil.success(HttpStatus.OK, "주간 Summary + 해당주 월화수목금토일 수입지출 조회 성공", summary);
    }
    @GetMapping("/lwtw/{year}/{weekNum}")
    public ResponseEntity<ApiResponse<WeeklyLWTWAmountDto>> getWeeklyLWTWAmount(
            @PathVariable int year,
            @PathVariable int weekNum,
            @AuthenticationPrincipal CurrentUser currentUser) {
        String userId = currentUser.getUserId();
        WeeklyLWTWAmountDto lwtw = weeklyStatService.getWeeklyLastWeekAndThisWeekAmount(userId, year, weekNum);
        return ApiResponseUtil.success(HttpStatus.OK, "저번주 불필요 지출, 이번주불필요지출의 각각최대 조회 성공", lwtw);
    }
}
