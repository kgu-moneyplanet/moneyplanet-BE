package com.money.app.tx.controller;

import com.money.app.security.CurrentUser;
import com.money.app.tx.dto.ReportUpdateDto;
import com.money.app.tx.dto.TxCreateRequestDto;
import com.money.app.tx.dto.TxResponseDto;
import com.money.app.tx.dto.TxUpdateDto;
import com.money.app.tx.service.TxService;
import com.money.app.util.response.ApiResponse;
import com.money.app.util.response.ApiResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tx")
public class TxController {

    private final TxService txService;

    public TxController(TxService txService){this.txService = txService;}

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createTxWithReport(
            @RequestBody TxCreateRequestDto dto,
            @AuthenticationPrincipal CurrentUser currentUser) {
        String userId = currentUser.getUserId();
        txService.createTxWithReport(userId, dto);
        return ApiResponseUtil.success(HttpStatus.CREATED, "Tx 생성 성공");
    }

    @PutMapping("/{txId}")
    public ResponseEntity<ApiResponse<Void>> updateTxWithReport(
            @PathVariable String txId,
            @RequestBody TxUpdateDto dto,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        txService.updateTxWithReport(userId, txId, dto);
        return ApiResponseUtil.success(HttpStatus.OK,"Tx 수정 성공");
    }

    @DeleteMapping("/{txId}")
    public ResponseEntity<ApiResponse<Void>> deleteTxWithReport(
            @PathVariable String txId,
            @AuthenticationPrincipal CurrentUser currentUser) {
        String userId = currentUser.getUserId();
        txService.deleteTxWithReport(userId, txId);
        return ApiResponseUtil.success(HttpStatus.OK, "TX 삭제 성공");
    }

    @GetMapping("/{txId}")
    public ResponseEntity<ApiResponse<TxResponseDto>> getTxWithReportById(
            @PathVariable String txId,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        txService.getTxWithReportById(userId, txId);
        return ApiResponseUtil.success(HttpStatus.OK, "Tx 조회 성공");
    }

    @GetMapping("/list/{txDate}")
    public ResponseEntity<ApiResponse<List<TxResponseDto>>> getTxTodayByTxDate(
            @PathVariable LocalDate txDate,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        txService.getTxTodayByTxDate(userId, txDate);
        return ApiResponseUtil.success(HttpStatus.OK,"Tx 리스트 조회 성공");
    }

    @PutMapping("/report/{txId}")
    public ResponseEntity<ApiResponse<Void>> updateReportByTxId(
            @PathVariable String txId,
            @RequestBody ReportUpdateDto dto,
            @AuthenticationPrincipal CurrentUser currentUser){
        String userId = currentUser.getUserId();
        txService.updateReportById(userId, txId, dto);
        return ApiResponseUtil.success(HttpStatus.OK, "Report 수정 성공");
    }

}
