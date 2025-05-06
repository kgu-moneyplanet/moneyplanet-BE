package com.money.app.module.stat.weeklystat.repository;

import com.money.app.module.category.domain.Category;
import com.money.app.module.stat.weeklystat.domain.WeeklyStat;
import com.money.app.module.user.domain.User;
import com.money.app.util.common.dto.AbcStatDto;
import com.money.app.util.common.dto.CateAbcStatDto;
import com.money.app.util.common.dto.CategoryStatDto;
import com.money.app.util.common.dto.SumStatDto;
import com.money.app.util.common.enumtype.AbcType;
import com.money.app.util.common.enumtype.TxType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WeeklyStatRepository  extends JpaRepository<WeeklyStat, Long> {
    @Query("""
    SELECT ws FROM WeeklyStat ws
    WHERE ws.user = :user
      AND ws.year = :year
      AND ws.weekNum = :weekNum
      AND ws.type = :type
      AND (
            (:category IS NULL AND ws.category IS NULL) OR
            (:category IS NOT NULL AND ws.category = :category)
          )
      AND (
            (:abc IS NULL AND ws.abc IS NULL) OR
            (:abc IS NOT NULL AND ws.abc = :abc)
          )
""")
    Optional<WeeklyStat> findStat(User user, int year, int weekNum, TxType type, Category category, AbcType abc);

    @Query("""
    SELECT new com.money.app.util.common.dto.AbcStatDto(ws.abc, SUM(ws.amount))
    FROM WeeklyStat ws
    WHERE ws.user = :user
      AND ws.year = :year
      AND ws.weekNum = :weekNum
      AND ws.type = :type
      AND ws.category IS NULL
      AND ws.abc IS NOT NULL
    GROUP BY ws.abc
""")
    List<AbcStatDto> findAbcStats(User user, int year, int weekNum, TxType type);

    @Query("""
    SELECT new com.money.app.util.common.dto.SumStatDto(ws.type, ws.amount)
    FROM WeeklyStat ws
    WHERE ws.user = :user
      AND ws.year = :year
      AND ws.weekNum = :weekNum
      AND ws.category IS NULL
      AND ws.abc IS NULL
""")
    List<SumStatDto> findSumStats(User user, int year, int weekNum);

    @Query("""
    SELECT new com.money.app.util.common.dto.CategoryStatDto(ws.category.id, ws.category.name, ws.amount)
    FROM WeeklyStat ws
    WHERE ws.user = :user
      AND ws.year = :year
      AND ws.weekNum = :weekNum
      AND ws.category IS NOT NULL
      AND ws.abc IS NULL
      ORDER BY ws.amount DESC
""")
    List<CategoryStatDto> findCategoryStats(User user, int year, int weekNum);

    @Query("""
    SELECT new com.money.app.util.common.dto.CateAbcStatDto(ws.category.id, ws.category.name, ws.abc, ws.amount)
    FROM WeeklyStat ws
    WHERE ws.user = :user
      AND ws.year = :year
      AND ws.weekNum = :weekNum
      AND ws.category IS NOT NULL
      AND ws.abc IS NOT NULL
      ORDER BY ws.amount DESC, ws.abc ASC
""")
    List<CateAbcStatDto> findCateAbcStats(User user, int year, int weekNum);

    @Query("""
    SELECT new com.money.app.util.common.dto.CateAbcStatDto(ws.category.id, ws.category.name, ws.abc, ws.amount)
    FROM WeeklyStat ws
    WHERE ws.user = :user
      AND ws.year = :year
      AND ws.weekNum = :weekNum
      AND ws.category IS NOT NULL
      AND ws.abc =:abc
      ORDER BY ws.amount DESC, ws.abc ASC
""")
    List<CateAbcStatDto> findCateAbcStatsWithAbc(User user, int year, int weekNum, AbcType abc);

    @Query("""
    SELECT new com.money.app.util.common.dto.CateAbcStatDto(ws.category.id, ws.category.name, ws.abc, ws.amount)
    FROM WeeklyStat ws
    WHERE ws.user = :user
      AND ws.year = :year
      AND ws.weekNum = :weekNum
      AND ws.category = :category
      AND ws.abc =:abc
      ORDER BY ws.amount DESC, ws.abc ASC
""")
    CateAbcStatDto findCateAbcStatsWithCategoryAndAbc(User user, int year, int weekNum, AbcType abc, Category category);
}

