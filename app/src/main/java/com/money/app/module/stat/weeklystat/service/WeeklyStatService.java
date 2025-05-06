package com.money.app.module.stat.weeklystat.service;

import com.money.app.module.category.domain.Category;
import com.money.app.module.category.service.CategoryService;
import com.money.app.module.stat.weeklystat.domain.WeeklyStat;
import com.money.app.module.stat.weeklystat.dto.WeeklyLWTWAmountDto;
import com.money.app.module.stat.weeklystat.dto.WeeklySumStatListResponseDto;
import com.money.app.module.stat.weeklystat.dto.WeeklySummaryStatDto;
import com.money.app.module.stat.weeklystat.repository.WeeklyStatRepository;
import com.money.app.module.tx.domain.Tx;
import com.money.app.module.user.domain.User;
import com.money.app.module.user.service.UserService;
import com.money.app.util.common.dto.*;
import com.money.app.util.common.enumtype.AbcType;
import com.money.app.util.common.enumtype.TxType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;


@Service
public class WeeklyStatService {
    private final WeeklyStatRepository weeklyStatRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    public WeeklyStatService(WeeklyStatRepository weeklyStatRepository, CategoryService categoryService,
                            UserService userService) {
        this.weeklyStatRepository = weeklyStatRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }
    @Transactional
    public void upsertWeeklyStat(User user, int year, int weekNum, TxType type,
                                Category category, AbcType abc, Long amount) {
        Optional<WeeklyStat> weeklyStat = weeklyStatRepository
                .findStat(user, year, weekNum, type, category, abc);
        if (weeklyStat.isPresent()) {
            // 기존 값이 있으면 누적 업데이트
            WeeklyStat stat = weeklyStat.get();
            stat.update(stat.getAmount() + amount);
        } else {
            // 없으면 새로 생성
            WeeklyStat stat = WeeklyStat.create(user, year, weekNum, type, category, abc, amount);
            weeklyStatRepository.save(stat);
        }
    }

    public void applyUpsertWeeklyStat(Tx tx, User user, Category category, Boolean status) {
        long addorminus = status ? tx.getAmount() : -tx.getAmount();
        LocalDate date = tx.getTxDate();
        TxType type = tx.getType();
        AbcType abc = tx.getAbc();
        WeekFields weekFields = WeekFields.of(Locale.KOREA);
        int year = date.getYear();
        int weekNum = date.get(weekFields.weekOfWeekBasedYear());

        upsertWeeklyStat(user, year, weekNum, type, null, null, addorminus); // 전체
        if (type == TxType.EXPENSE && abc != null) {
            upsertWeeklyStat(user, year, weekNum, type, null, abc, addorminus); // ABC별
        }
        upsertWeeklyStat(user, year, weekNum, type, category, null, addorminus); // 카테고리별
        if (type == TxType.EXPENSE && abc != null) {
            upsertWeeklyStat(user, year, weekNum, type, category, abc, addorminus); // 카테+ABC
        }
    }

    @Transactional(readOnly = true)
    public AbcStatResponseDto getWeeklyAbc(String userId, int year, int weekNum) {
        User user = userService.getUserEntityById(userId);
        List<AbcStatDto> stats = weeklyStatRepository.findAbcStats(user, year, weekNum, TxType.EXPENSE);

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
    public SumStatResponseDto getWeeklySum(String userId, int year, int weekNum) {
        User user = userService.getUserEntityById(userId);
        List<SumStatDto> stats = weeklyStatRepository.findSumStats(user, year, weekNum);
        Map<TxType, Long> map = new HashMap<>();
        for (SumStatDto dto : stats) {
            map.put(dto.getType(), dto.getSum());
        }
        Long totalExpense = map.getOrDefault(TxType.EXPENSE, 0L);
        Long totalIncome = map.getOrDefault(TxType.INCOME, 0L);
        return new SumStatResponseDto(totalExpense, totalIncome, totalExpense + totalIncome);
    }

    @Transactional(readOnly = true)
    public CategoryStatResponseDto getWeeklyCategory(String userId, int year, int weekNum) {
        User user = userService.getUserEntityById(userId);
        List<CategoryStatDto> stats = weeklyStatRepository.findCategoryStats(user, year, weekNum);
        Long totalAmount = 0L;
        totalAmount = stats.stream()
                .mapToLong(CategoryStatDto::getAmount)
                .sum();
        return new CategoryStatResponseDto(stats, totalAmount);
    }

    @Transactional(readOnly = true)
    public CateAbcResoponseDto getWeeklyCateAbc(String userId, int year, int weekNum) {
        User user = userService.getUserEntityById(userId);
        List<CateAbcStatDto> stats = weeklyStatRepository.findCateAbcStats(user, year, weekNum);
        Long totalAmount = 0L;
        totalAmount = stats.stream()
                .mapToLong(CateAbcStatDto::getAmount)
                .sum();
        return new CateAbcResoponseDto(stats, totalAmount);
    }

    @Transactional(readOnly = true)
    public List<WeeklySumStatListResponseDto> getWeeklySumList(String userId, int year, int weekNum) {
        User user = userService.getUserEntityById(userId);
        List<WeeklySumStatListResponseDto> sumList = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            // 최근 7주를 위에서부터 출력 (가장 오래된 주부터)
            int targetWeek = weekNum - i;
            LocalDate wednesday = LocalDate.of(year, 1, 4)
                    .with(WeekFields.ISO.weekOfWeekBasedYear(), targetWeek)
                    .with(WeekFields.ISO.dayOfWeek(), 3);  // 수요일 (3)
            int targetYear = wednesday.getYear();
            int targetMonth = wednesday.getMonthValue();
            int weekOfMonth = wednesday.get(WeekFields.of(Locale.KOREA).weekOfMonth());
            // 통계 조회
            List<SumStatDto> stats = weeklyStatRepository.findSumStats(user, targetYear, targetWeek);
            Map<TxType, Long> map = new HashMap<>();
            for (SumStatDto dto : stats) {
                map.put(dto.getType(), dto.getSum());
            }
            long expense = map.getOrDefault(TxType.EXPENSE, 0L);
            long income = map.getOrDefault(TxType.INCOME, 0L);

            sumList.add(new WeeklySumStatListResponseDto(
                    targetYear,
                    targetMonth,
                    weekOfMonth,
                    expense,
                    income,
                    expense + income
            ));
        }
        return sumList;
    }

    @Transactional(readOnly = true)
    public WeeklySummaryStatDto getWeeklySummary(String userId, int year, int weekNum) {
        User user = userService.getUserEntityById(userId);
        List<WeeklySumStatListResponseDto> sumList = getWeeklySumList(userId, year, weekNum);
        SumStatResponseDto sumStat = getWeeklySum(userId, year, weekNum);
        CategoryStatResponseDto categoryStat = getWeeklyCategory(userId,year,weekNum);
        AbcStatResponseDto abcStat = getWeeklyAbc(userId,year,weekNum);
        CateAbcResoponseDto cateAbcStat = getWeeklyCateAbc(userId,year,weekNum);
        return new WeeklySummaryStatDto(sumList, sumStat, categoryStat, abcStat, cateAbcStat);
    }

    public WeeklyLWTWAmountDto getWeeklyLastWeekAndThisWeekAmount(String userId, int year, int weekNum) {
        User user = userService.getUserEntityById(userId);
        List<CateAbcStatDto> lastWeek = weeklyStatRepository.findCateAbcStatsWithAbc(user, year, weekNum - 1 ,AbcType.C);
        CateAbcStatDto lastWeekInfo = lastWeek.isEmpty() ? null : lastWeek.get(0);
        List<CateAbcStatDto> thisWeek = weeklyStatRepository.findCateAbcStatsWithAbc(user, year, weekNum, AbcType.C);
        CateAbcStatDto thisWeekInfo = thisWeek.isEmpty() ? null : thisWeek.get(0);
        String lastCategoryName = lastWeekInfo != null ? lastWeekInfo.getCategoryName() : "가계부를 등록해주세요!";
        Long lastAmount = lastWeekInfo != null ? lastWeekInfo.getAmount() : 0L;
        String thisCategoryName = thisWeekInfo != null ? thisWeekInfo.getCategoryName() : "가계부를 등록해주세요!";
        Long thisAmount = thisWeekInfo != null ? thisWeekInfo.getAmount() : 0L;
        Long diff = 0L;
        if (thisWeekInfo != null) {
            Category category = categoryService.getCategoryEntityById(thisWeekInfo.getCategoryId());
            CateAbcStatDto lastWeekInfoForCalc = weeklyStatRepository.findCateAbcStatsWithCategoryAndAbc(
                    user, year, weekNum - 1, AbcType.C, category);
            Long lastAmountForCalc = lastWeekInfoForCalc != null ? lastWeekInfoForCalc.getAmount() : 0L;
            diff = thisAmount - lastAmountForCalc;
        }
        return new WeeklyLWTWAmountDto(
                lastCategoryName,
                lastAmount,
                thisCategoryName,
                thisAmount,
                diff
        );
    }

}
