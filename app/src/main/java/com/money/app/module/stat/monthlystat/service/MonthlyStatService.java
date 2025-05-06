package com.money.app.module.stat.monthlystat.service;

import com.money.app.module.category.domain.Category;
import com.money.app.module.category.service.CategoryService;
import com.money.app.module.stat.monthlystat.dto.MonthlySummaryStatDto;
import com.money.app.module.stat.monthlystat.repository.MonthlyStatRepository;
import com.money.app.module.stat.monthlystat.domain.MonthlyStat;
import com.money.app.module.stat.monthlystat.dto.MonthlySumStatListResponseDto;
import com.money.app.module.tx.domain.Tx;
import com.money.app.module.user.domain.User;
import com.money.app.module.user.service.UserService;
import com.money.app.util.common.dto.*;
import com.money.app.util.common.enumtype.AbcType;
import com.money.app.util.common.enumtype.TxType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;


@Service
public class MonthlyStatService {
    private final MonthlyStatRepository monthlyStatRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    public MonthlyStatService(MonthlyStatRepository monthlyStatRepository, CategoryService categoryService,
                              UserService userService) {
        this.monthlyStatRepository = monthlyStatRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }
    @Transactional
    public void upsertMonthlyStat(User user, int year, int month, TxType type,
                                Category category, AbcType abc, Long amount) {
        Optional<MonthlyStat> monthlyStat = monthlyStatRepository
                .findStat(user, year, month, type, category, abc);
        if (monthlyStat.isPresent()) {
            // 기존 값이 있으면 누적 업데이트
            MonthlyStat stat = monthlyStat.get();
            stat.update(stat.getAmount() + amount);
        } else {
            // 없으면 새로 생성
            MonthlyStat stat = MonthlyStat.create(user, year, month, type, category, abc, amount);
            monthlyStatRepository.save(stat);
        }
    }

    public void applyUpsertMonthlyStat(Tx tx, User user, Category category, Boolean status) {
        long addorminus = status ? tx.getAmount() : -tx.getAmount();
        LocalDate date = tx.getTxDate();
        TxType type = tx.getType();
        AbcType abc = tx.getAbc();
        int year = date.getYear();
        int month = date.getMonthValue();

        upsertMonthlyStat(user, year, month, type, null, null, addorminus); // 전체
        if (type == TxType.EXPENSE && abc != null) {
            upsertMonthlyStat(user, year, month, type, null, abc, addorminus); // ABC별
        }
        upsertMonthlyStat(user, year, month, type, category, null, addorminus); // 카테고리별
        if (type == TxType.EXPENSE && abc != null) {
            upsertMonthlyStat(user, year, month, type, category, abc, addorminus); // 카테+ABC
        }
    }

    @Transactional(readOnly = true)
    public AbcStatResponseDto getMonthlyAbc(String userId, int year, int month) {
        User user = userService.getUserEntityById(userId);
        List<AbcStatDto> stats = monthlyStatRepository.findAbcStats(user, year, month, TxType.EXPENSE);

        Map<AbcType, Long> map = new HashMap<>();
        for (AbcStatDto dto : stats) {
            map.put(dto.getAbc(), dto.getAmount());
        }
        Long totalA = map.getOrDefault(AbcType.A, 0L);
        Long totalB = map.getOrDefault(AbcType.B, 0L);
        Long totalC = map.getOrDefault(AbcType.C, 0L);
        Long totalExpense = totalA + totalB + totalC;

        return new AbcStatResponseDto(totalA, totalB, totalC, totalExpense);
    }

    @Transactional(readOnly = true)
    public SumStatResponseDto getMonthlySum(String userId, int year, int month) {
        User user = userService.getUserEntityById(userId);
        List<SumStatDto> stats = monthlyStatRepository.findSumStats(user, year, month);
        Map<TxType, Long> map = new HashMap<>();
        for (SumStatDto dto : stats) {
            map.put(dto.getType(), dto.getSum());
        }
        Long totalExpense = map.getOrDefault(TxType.EXPENSE, 0L);
        Long totalIncome = map.getOrDefault(TxType.INCOME, 0L);
        return new SumStatResponseDto(totalExpense, totalIncome, totalExpense + totalIncome);
    }

    @Transactional(readOnly = true)
    public CategoryStatResponseDto getMonthlyCategory(String userId, int year, int month) {
        User user = userService.getUserEntityById(userId);
        List<CategoryStatDto> stats = monthlyStatRepository.findCategoryStats(user, year, month);
        Long totalAmount = 0L;
        totalAmount = stats.stream()
                .mapToLong(CategoryStatDto::getAmount)
                .sum();
        return new CategoryStatResponseDto(stats, totalAmount);
    }

    @Transactional(readOnly = true)
    public CateAbcResoponseDto getMonthlyCateAbc(String userId, int year, int month) {
        User user = userService.getUserEntityById(userId);
        List<CateAbcStatDto> stats = monthlyStatRepository.findCateAbcStats(user, year, month);
        Long totalAmount = 0L;
        totalAmount = stats.stream()
                .mapToLong(CateAbcStatDto::getAmount)
                .sum();
        return new CateAbcResoponseDto(stats, totalAmount);
    }

    @Transactional(readOnly = true)
    public List<MonthlySumStatListResponseDto> getMonthlySumList(String userId, int year, int month) {
        User user = userService.getUserEntityById(userId);
        List<MonthlySumStatListResponseDto> sumList = new ArrayList<>();
        YearMonth start = YearMonth.of(year, month);
        YearMonth end = start.minusMonths(6);  // 6개월 전까지 포함
        for (YearMonth ym = end; !ym.isAfter(start); ym = ym.plusMonths(1)) {
            int targetYear = ym.getYear();
            int targetMonth = ym.getMonthValue();
            // 통계 조회
            List<SumStatDto> stats = monthlyStatRepository.findSumStats(user, targetYear, targetMonth);
            Map<TxType, Long> map = new HashMap<>();
            for (SumStatDto dto : stats) {
                map.put(dto.getType(), dto.getSum());
            }
            long expense = map.getOrDefault(TxType.EXPENSE, 0L);
            long income = map.getOrDefault(TxType.INCOME, 0L);

            sumList.add(new MonthlySumStatListResponseDto(
                    targetYear,
                    targetMonth,
                    expense,
                    income,
                    expense + income
            ));
        }
        return sumList;
    }

    @Transactional(readOnly = true)
    public MonthlySummaryStatDto getMonthlySummary(String userId, int year, int month) {
        User user = userService.getUserEntityById(userId);
        List<MonthlySumStatListResponseDto> sumList = getMonthlySumList(userId, year, month);
        SumStatResponseDto sumStat = getMonthlySum(userId, year, month);
        CategoryStatResponseDto categoryStat = getMonthlyCategory(userId,year,month);
        AbcStatResponseDto abcStat = getMonthlyAbc(userId,year,month);
        CateAbcResoponseDto cateAbcStat = getMonthlyCateAbc(userId,year,month);
        return new MonthlySummaryStatDto(sumList, sumStat, categoryStat, abcStat, cateAbcStat);
    }
}
