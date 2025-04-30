package com.money.app.tx.service;

import com.money.app.category.domain.Category;
import com.money.app.category.service.CategoryService;
import com.money.app.tx.domain.Report;
import com.money.app.tx.domain.Tx;
import com.money.app.tx.domain.TxType;
import com.money.app.tx.dto.*;
import com.money.app.tx.repository.ReportRepository;
import com.money.app.tx.repository.TxRepository;
import com.money.app.user.domain.User;
import com.money.app.user.service.UserService;
import com.money.app.util.exception.CustomException;
import com.money.app.util.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class TxService {
    private final TxRepository txRepository;
    private final ReportRepository reportRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    public TxService(TxRepository txRepository, ReportRepository reportRepository,
                     CategoryService categoryService, UserService userService){
        this.txRepository = txRepository;
        this.reportRepository = reportRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }
    @Transactional
    public void createTxWithReport(String userId, TxCreateRequestDto dto) {
        // txDate, type, categoryId, method, amount가 제대로 입력되었는지 확인(필수요소)
        if (dto.getTxDate() == null || dto.getType() == null || dto.getAmount() == null ||
                dto.getCategoryId() == null || dto.getMethod() == null) {
            throw new CustomException(ErrorCode.INVALID_FORMAT);
        }
        // 카테고리 올바르게 입력했는지 검토
        Category category = categoryService.getCategoryEntityById(dto.getCategoryId());
        // User 정보 조회
        User user = userService.getUserEntityById(userId);
        // 저장 Dto -> Entity로
        TxCreateDto txCreateDto = new TxCreateDto();
        txCreateDto.setUserId(user.getId());
        txCreateDto.setTxDate(dto.getTxDate());
        txCreateDto.setType(dto.getType());
        txCreateDto.setCategoryId(dto.getCategoryId());
        txCreateDto.setAbc(dto.getAbc());
        txCreateDto.setAmount(dto.getAmount());
        txCreateDto.setMethod(dto.getMethod());
        txCreateDto.setContent(dto.getContent());
        txCreateDto.setMemo(dto.getMemo());
        Tx tx = Tx.create(user, category, txCreateDto);
        txRepository.save(tx);
        // type이 expensive일때 Report 생성
        if (dto.getType() == TxType.EXPENSE) {
            ReportCreateDto reportCreateDto = new ReportCreateDto();
            reportCreateDto.setTxId(tx.getId());
            reportCreateDto.setAbc(tx.getAbc());
            reportCreateDto.setReason(dto.getReason());
            reportCreateDto.setFeedback(dto.getFeedback());
            Report report = Report.create(tx, reportCreateDto);
            reportRepository.save(report);
        }
        // 여기에다가 통계테이블 insert, update 코드 주입예정
        // 일일, 주간, 월간

    }

    @Transactional
    public void updateTxWithReport(String userId, String txId, TxUpdateDto dto){
        // ID로 기존 Tx 조회
        Tx tx = txRepository.findById(txId)
                .orElseThrow(() -> new CustomException(ErrorCode.TX_NOT_FOUNT));
        if (!tx.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND); // 사용자가 소유자가 아님
        }
        // Category 확인
        Category category = categoryService.getCategoryEntityById(dto.getCategoryId());
        tx.update(category, dto);
        if (dto.getType() == TxType.EXPENSE){
            Report report = reportRepository.findByTxId(tx.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));
            report.updateAbc(dto.getAbc());
        }
        // 일일, 주간, 월간 통계 테이블의 위 관련 사항들 값을 변경해야함
        // -> 업데이트로 값들 수정 만약 없으면 insert 이거 하나면 delete
    }

    @Transactional
    public void deleteTxWithReport(String userId, String txId){
        Tx tx = txRepository.findById(txId)
                .orElseThrow(()-> new CustomException(ErrorCode.TX_NOT_FOUNT));
        if (!tx.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND); // 사용자가 소유자가 아님
        }
        txRepository.delete(tx);
        // cascade로 report 삭제
        // 통계테이블 업뎃
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
        report.update(dto);
    }
}


