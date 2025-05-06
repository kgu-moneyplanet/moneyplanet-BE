package com.money.app.stat.dailystat.service;

import com.money.app.category.domain.Category;
import com.money.app.category.service.CategoryService;
import com.money.app.stat.dailystat.domain.DailyStat;
import com.money.app.stat.dailystat.dto.DailySumStatListResponseDto;
import com.money.app.stat.dailystat.dto.DailySummaryStatDto;
import com.money.app.stat.dailystat.repository.DailyStatRepository;
import com.money.app.tx.domain.Tx;
import com.money.app.user.domain.User;
import com.money.app.user.service.UserService;
import com.money.app.util.common.dto.*;
import com.money.app.util.common.enumtype.AbcType;
import com.money.app.util.common.enumtype.TxType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DailyStatService {
    private final DailyStatRepository dailyStatRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    public DailyStatService(DailyStatRepository dailyStatRepository, CategoryService categoryService,
                            UserService userService) {
        this.dailyStatRepository = dailyStatRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }
    @Transactional
    public void upsertDailyStat(User user, LocalDate statDate, TxType type,
                                Category category, AbcType abc, Long amount) {

        Optional<DailyStat> dailyStat = dailyStatRepository
                .findStat(user, statDate, type, category, abc);
        if (dailyStat.isPresent()) {
            // 기존 값이 있으면 누적 업데이트
            DailyStat stat = dailyStat.get();
            stat.update(stat.getAmount() + amount);
        } else {
            // 없으면 새로 생성
            DailyStat stat = DailyStat.create(user, statDate, type, category, abc, amount);
            dailyStatRepository.save(stat);
        }
    }

    public void applyUpsertDailyStat(Tx tx, User user, Category category, Boolean status) {
        long addorminus = status ? tx.getAmount() : -tx.getAmount();
        LocalDate date = tx.getTxDate();
        TxType type = tx.getType();
        AbcType abc = tx.getAbc();

        upsertDailyStat(user, date, type, null, null, addorminus); // 전체
        if (type == TxType.EXPENSE && abc != null) {
            upsertDailyStat(user, date, type, null, abc, addorminus); // ABC별
        }
        upsertDailyStat(user, date, type, category, null, addorminus); // 카테고리별
        if (type == TxType.EXPENSE && abc != null) {
            upsertDailyStat(user, date, type, category, abc, addorminus); // 카테+ABC
        }
    }

    @Transactional(readOnly = true)
    public AbcStatResponseDto getDailyAbc(String userId, LocalDate statDate) {
        User user = userService.getUserEntityById(userId);
        List<AbcStatDto> stats = dailyStatRepository.findAbcStats(user, statDate, TxType.EXPENSE);

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
    public SumStatResponseDto getDailySum(String userId, LocalDate statDate) {
        User user = userService.getUserEntityById(userId);
        List<SumStatDto> stats = dailyStatRepository.findSumStats(user, statDate);
        Map<TxType, Long> map = new HashMap<>();
        for (SumStatDto dto : stats) {
            map.put(dto.getType(), dto.getSum());
        }
        Long totalExpense = map.getOrDefault(TxType.EXPENSE, 0L);
        Long totalIncome = map.getOrDefault(TxType.INCOME, 0L);
        return new SumStatResponseDto(totalExpense, totalIncome, totalExpense + totalIncome);
    }

    @Transactional(readOnly = true)
    public CategoryStatResponseDto getDailyCategory(String userId, LocalDate statDate) {
        User user = userService.getUserEntityById(userId);
        List<CategoryStatDto> stats = dailyStatRepository.findCategoryStats(user, statDate);
        Long totalAmount = 0L;
        totalAmount = stats.stream()
                .mapToLong(CategoryStatDto::getAmount)
                .sum();
        return new CategoryStatResponseDto(stats, totalAmount);
    }

    @Transactional(readOnly = true)
    public CateAbcResoponseDto getDailyCateAbc(String userId, LocalDate statDate) {
        User user = userService.getUserEntityById(userId);
        List<CateAbcStatDto> stats = dailyStatRepository.findCateAbcStats(user, statDate);
        Long totalAmount = 0L;
        totalAmount = stats.stream()
                .mapToLong(CateAbcStatDto::getAmount)
                .sum();
        return new CateAbcResoponseDto(stats, totalAmount);
    }

    @Transactional(readOnly = true)
    public List<DailySumStatListResponseDto> getDailySumList(String userId, LocalDate statDate) {
        User user = userService.getUserEntityById(userId);
        // 기준 날짜의 월요일 ~ 일요일 구하기
        LocalDate monday = statDate.with(DayOfWeek.MONDAY);
        LocalDate sunday = statDate.with(DayOfWeek.SUNDAY);
        // 월~일 날짜 리스트 생성
        List<LocalDate> weekDates = monday.datesUntil(sunday.plusDays(1)).collect(Collectors.toList());
        // 통계 리스트 생성
        List<DailySumStatListResponseDto> sumList = new ArrayList<>();
        for (LocalDate date : weekDates) {
            List<SumStatDto> stats = dailyStatRepository.findSumStats(user, date);
            Map<TxType, Long> map = new HashMap<>();
            for (SumStatDto dto : stats) {
                map.put(dto.getType(), dto.getSum());
            }
            Long totalExpense = map.getOrDefault(TxType.EXPENSE, 0L);
            Long totalIncome = map.getOrDefault(TxType.INCOME, 0L);
            sumList.add(new DailySumStatListResponseDto(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                    date,totalExpense, totalIncome, totalExpense + totalIncome));
        }
        return sumList;
    }

    @Transactional(readOnly = true)
    public DailySummaryStatDto getDailySummary(String userId, LocalDate statDate) {
        User user = userService.getUserEntityById(userId);
        List<DailySumStatListResponseDto> sumList = getDailySumList(userId, statDate);
        SumStatResponseDto sumStat = getDailySum(userId, statDate);
        CategoryStatResponseDto categoryStat = getDailyCategory(userId,statDate);
        AbcStatResponseDto abcStat = getDailyAbc(userId,statDate);
        CateAbcResoponseDto cateAbcStat = getDailyCateAbc(userId,statDate);
        return new DailySummaryStatDto(sumList, sumStat, categoryStat, abcStat, cateAbcStat);
    }
}
