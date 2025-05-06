package com.money.app.tx.service;

import com.money.app.category.domain.Category;
import com.money.app.category.service.CategoryService;
import com.money.app.stat.dailystat.service.DailyStatService;
import com.money.app.stat.monthlystat.service.MonthlyStatService;
import com.money.app.stat.weeklystat.service.WeeklyStatService;
import com.money.app.tx.domain.Report;
import com.money.app.tx.domain.Tx;
import com.money.app.tx.dto.*;
import com.money.app.tx.repository.ReportRepository;
import com.money.app.tx.repository.TxRepository;
import com.money.app.user.domain.User;
import com.money.app.user.service.UserService;
import com.money.app.util.common.enumtype.AbcType;
import com.money.app.util.common.enumtype.MethodType;
import com.money.app.util.common.enumtype.TxType;
import com.money.app.util.exception.CustomException;
import com.money.app.util.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Service
public class TxService {
    private final TxRepository txRepository;
    private final ReportRepository reportRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final DailyStatService dailyStatService;
    private final WeeklyStatService weeklyStatService;
    private final MonthlyStatService monthlyStatService;

    public TxService(TxRepository txRepository, ReportRepository reportRepository,
                     CategoryService categoryService, UserService userService, DailyStatService dailyStatService
                    , WeeklyStatService weeklyStatService, MonthlyStatService monthlyStatService) {
        this.txRepository = txRepository;
        this.reportRepository = reportRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        this.dailyStatService = dailyStatService;
        this.weeklyStatService = weeklyStatService;
        this.monthlyStatService = monthlyStatService;
    }
    @Transactional
    public void createTxWithReport(String userId, TxCreateDto dto) {
        // txDate, type, categoryId, method, amount가 제대로 입력되었는지 확인(필수요소)
        if (dto.getTxDate() == null || dto.getType() == null || dto.getAmount() == null ||
                dto.getCategoryId() == null || dto.getMethod() == null) {
            throw new CustomException(ErrorCode.INVALID_FORMAT);
        }
        Set<AbcType> validAbcs = Set.of(AbcType.A, AbcType.B, AbcType.C);
        if (dto.getAbc() != null && !validAbcs.contains(dto.getAbc())) {
            throw new CustomException(ErrorCode.INVALID_FORMAT);
        }
        Set<MethodType> validMethods = Set.of(MethodType.SALARY, MethodType.CASH, MethodType.BANK_TRANSFER,
                                        MethodType.CHECK, MethodType.CREDIT, MethodType.ETC);
        if (dto.getMethod() != null && !validMethods.contains(dto.getMethod())) {
            throw new CustomException(ErrorCode.INVALID_FORMAT);
        }
        Set<TxType> validTxs = Set.of(TxType.EXPENSE, TxType.INCOME);
        if (dto.getType() != null && !validTxs.contains(dto.getType())) {
            throw new CustomException(ErrorCode.INVALID_FORMAT);
        }
        if (dto.getType() == TxType.INCOME) {
            dto.setAbc(null);
        }
        // 카테고리 올바르게 입력했는지 검토
        Category category = categoryService.getCategoryEntityById(dto.getCategoryId());
        // User 정보 조회
        User user = userService.getUserEntityById(userId);
        //tx 생성
        Tx tx = Tx.create(user, category, dto.getTxDate(), dto.getType(), dto.getAbc(), dto.getAmount(), dto.getMethod(),
                dto.getContent(), dto.getMemo());
        txRepository.save(tx);
        // type이 expensive일때 Report 생성
        if (dto.getType() == TxType.EXPENSE) {
            Report report = Report.create(tx, dto.getAbc(),dto.getReason(),dto.getFeedback());
            reportRepository.save(report);
        }
        // 일일 Stat 업뎃
        dailyStatService.applyUpsertDailyStat(tx, user, category, Boolean.TRUE);
        // 주간 Stat 업뎃
        weeklyStatService.applyUpsertWeeklyStat(tx, user, category, Boolean.TRUE);
        // 월간 Stat 업뎃
        monthlyStatService.applyUpsertMonthlyStat(tx, user, category, Boolean.TRUE);
    }

    @Transactional
    public void updateTxWithReport(String userId, String txId, TxUpdateDto dto){
        // txDate, type, categoryId, method, amount가 제대로 입력되었는지 확인(필수요소)
        if (dto.getTxDate() == null || dto.getType() == null || dto.getAmount() == null ||
                dto.getCategoryId() == null || dto.getMethod() == null) {
            throw new CustomException(ErrorCode.INVALID_FORMAT);
        }
        Set<AbcType> validAbcs = Set.of(AbcType.A, AbcType.B, AbcType.C);
        if (dto.getAbc() != null && !validAbcs.contains(dto.getAbc())) {
            throw new CustomException(ErrorCode.INVALID_FORMAT);
        }
        Set<MethodType> validMethods = Set.of(MethodType.SALARY, MethodType.CASH, MethodType.BANK_TRANSFER,
                MethodType.CHECK, MethodType.CREDIT, MethodType.ETC);
        if (dto.getMethod() != null && !validMethods.contains(dto.getMethod())) {
            throw new CustomException(ErrorCode.INVALID_FORMAT);
        }
        Set<TxType> validTxs = Set.of(TxType.EXPENSE, TxType.INCOME);
        if (dto.getType() != null && !validTxs.contains(dto.getType())) {
            throw new CustomException(ErrorCode.INVALID_FORMAT);
        }
        if (dto.getType() == TxType.INCOME) {
            dto.setAbc(null);
        }
        // User 정보   조회
        User user = userService.getUserEntityById(userId);
        // ID로 기존 Tx 조회
        Tx tx = txRepository.findById(txId)
                .orElseThrow(() -> new CustomException(ErrorCode.TX_NOT_FOUNT));
        if (tx.getType() != dto.getType()) {
            throw new CustomException(ErrorCode.TX_UPDATED_CANCEL);
        }
        // Category 확인
        Category category = categoryService.getCategoryEntityById(dto.getCategoryId());
        //일일 삭제
        dailyStatService.applyUpsertDailyStat(tx, user, tx.getCategory(), Boolean.FALSE);
        //주간 삭제
        weeklyStatService.applyUpsertWeeklyStat(tx, user, tx.getCategory(), Boolean.FALSE);
        //월간 삭제
        monthlyStatService.applyUpsertMonthlyStat(tx, user, tx.getCategory(), Boolean.FALSE);
        //tx 업데이트
        tx.update(category, dto.getTxDate(),dto.getType(),dto.getAbc(),dto.getAmount(),dto.getMethod(),dto.getContent(),dto.getMemo());
        if (dto.getType() == TxType.EXPENSE){
            Report report = reportRepository.findByTxId(tx.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));
            report.updateAbc(dto.getAbc());
        }
        // 일일 업뎃
        dailyStatService.applyUpsertDailyStat(tx, user, category, Boolean.TRUE);
        // 주간 업뎃
        weeklyStatService.applyUpsertWeeklyStat(tx, user, category, Boolean.TRUE);
        // 월간 업뎃
        monthlyStatService.applyUpsertMonthlyStat(tx, user, category, Boolean.TRUE);
    }

    @Transactional
    public void deleteTxWithReport(String userId, String txId){
        Tx tx = txRepository.findById(txId)
                .orElseThrow(()-> new CustomException(ErrorCode.TX_NOT_FOUNT));
        // 카테고리 확인
        Category category = tx.getCategory();
        // User 정보 조회
        User user = userService.getUserEntityById(userId);
        //일일 삭제
        dailyStatService.applyUpsertDailyStat(tx, user, category, Boolean.FALSE);
        //주간 삭제
        weeklyStatService.applyUpsertWeeklyStat(tx, user, category, Boolean.FALSE);
        //월간 삭제
        monthlyStatService.applyUpsertMonthlyStat(tx, user, category, Boolean.FALSE);
        // cascade로 report 삭제 및 tx 삭제
        txRepository.delete(tx);

    }

    @Transactional(readOnly = true)
    public TxResponseDto getTxWithReportById(String userId, String txId) {
        Tx tx = txRepository.findById(txId)
                .orElseThrow(() -> new CustomException(ErrorCode.TX_NOT_FOUNT));
        if (!tx.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND); // 사용자가 소유자가 아님
        }
        ReportResponseDto reportResponseDto = null;
        if (tx.getType() == TxType.EXPENSE) {
            Report report = reportRepository.findByTxId(tx.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));
            reportResponseDto = new ReportResponseDto(
                    report.getAbc(),
                    report.getReason(),
                    report.getFeedback()
            );
        }
        return new TxResponseDto(
                tx.getId(),
                tx.getUser().getId(),
                tx.getTxDate(),
                tx.getType(),
                tx.getCategory().getId(),
                tx.getAbc(),
                tx.getAmount(),
                tx.getMethod(),
                tx.getContent(),
                tx.getMemo(),
                reportResponseDto
                );
    }
    @Transactional(readOnly = true)
    public List<TxResponseDto> getTxTodayByTxDate(String userId, LocalDate txDate){
        List<Tx> txList = txRepository.findAllByUserIdAndTxDate(userId, txDate);

        return txList.stream().map(tx -> {
            ReportResponseDto reportResponseDto = null;
            if (tx.getType() == TxType.EXPENSE && tx.getReport() != null) {
                Report report = tx.getReport();
                reportResponseDto = new ReportResponseDto(
                        report.getAbc(),
                        report.getReason(),
                        report.getFeedback()
                );
            }
            return new TxResponseDto(
                    tx.getId(),
                    tx.getUser().getId(),
                    tx.getTxDate(),
                    tx.getType(),
                    tx.getCategory().getId(),
                    tx.getAbc(),
                    tx.getAmount(),
                    tx.getMethod(),
                    tx.getContent(),
                    tx.getMemo(),
                    reportResponseDto
            );
        }).toList();
    }

    @Transactional
    public void updateReportById(String userId, String txId, ReportUpdateDto dto){
        Tx tx = txRepository.findById(txId)
                .orElseThrow(() -> new CustomException(ErrorCode.TX_NOT_FOUNT));
        Report report = reportRepository.findByTxId(txId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));
        if (!tx.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND); // 사용자가 소유자가 아님
        }
        report.update(dto.getReason(), dto.getFeedback());
    }
}


