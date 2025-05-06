package com.money.app.stat.dailystat.repository;

import com.money.app.category.domain.Category;
import com.money.app.stat.dailystat.domain.DailyStat;
import com.money.app.user.domain.User;
import com.money.app.util.common.dto.AbcStatDto;
import com.money.app.util.common.dto.CateAbcStatDto;
import com.money.app.util.common.dto.CategoryStatDto;
import com.money.app.util.common.dto.SumStatDto;
import com.money.app.util.common.enumtype.AbcType;
import com.money.app.util.common.enumtype.TxType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {
    @Query("""
    SELECT ds FROM DailyStat ds
    WHERE ds.user = :user
      AND ds.statDate = :statDate
      AND ds.type = :type
      AND (
            (:category IS NULL AND ds.category IS NULL) OR
            (:category IS NOT NULL AND ds.category = :category)
          )
      AND (
            (:abc IS NULL AND ds.abc IS NULL) OR
            (:abc IS NOT NULL AND ds.abc = :abc)
          )
""")
    Optional<DailyStat> findStat(User user, LocalDate statDate, TxType type, Category category, AbcType abc);

    @Query("""
    SELECT new com.money.app.util.common.dto.AbcStatDto(ds.abc, SUM(ds.amount))
    FROM DailyStat ds
    WHERE ds.user = :user
      AND ds.statDate = :statDate
      AND ds.type = :type
      AND ds.category IS NULL
      AND ds.abc IS NOT NULL
    GROUP BY ds.abc
""")
    List<AbcStatDto> findAbcStats(User user, LocalDate statDate, TxType type);

    @Query("""
    SELECT new com.money.app.util.common.dto.SumStatDto(ds.type, ds.amount)
    FROM DailyStat ds
    WHERE ds.user = :user
      AND ds.statDate = :statDate
      AND ds.category IS NULL
      AND ds.abc IS NULL
""")
    List<SumStatDto> findSumStats(User user, LocalDate statDate);

    @Query("""
    SELECT new com.money.app.util.common.dto.CategoryStatDto(ds.category.id, ds.category.name, ds.amount)
    FROM DailyStat ds
    WHERE ds.user = :user
      AND ds.statDate = :statDate
      AND ds.category IS NOT NULL
      AND ds.abc IS NULL
      ORDER BY ds.amount DESC
""")
    List<CategoryStatDto> findCategoryStats(User user, LocalDate statDate);

    @Query("""
    SELECT new com.money.app.util.common.dto.CateAbcStatDto(ds.category.id, ds.category.name, ds.abc, ds.amount)
    FROM DailyStat ds
    WHERE ds.user = :user
      AND ds.statDate = :statDate
      AND ds.category IS NOT NULL
      AND ds.abc IS NOT NULL
      ORDER BY ds.amount DESC, ds.abc ASC
""")
    List<CateAbcStatDto> findCateAbcStats(User user, LocalDate statDate);
}
