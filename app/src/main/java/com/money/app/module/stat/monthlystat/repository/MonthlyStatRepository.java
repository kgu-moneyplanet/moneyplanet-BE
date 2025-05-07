package com.money.app.module.stat.monthlystat.repository;

import com.money.app.module.category.domain.Category;
import com.money.app.module.stat.monthlystat.domain.MonthlyStat;
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

public interface MonthlyStatRepository extends JpaRepository<MonthlyStat, Long> {
    @Query("""
    SELECT ms FROM MonthlyStat ms
    WHERE ms.user = :user
      AND ms.year = :year
      AND ms.month = :month
      AND ms.type = :type
      AND (
            (:category IS NULL AND ms.category IS NULL) OR
            (:category IS NOT NULL AND ms.category = :category)
          )
      AND (
            (:abc IS NULL AND ms.abc IS NULL) OR
            (:abc IS NOT NULL AND ms.abc = :abc)
          )
""")
    Optional<MonthlyStat> findStat(User user, int year, int month, TxType type, Category category, AbcType abc);

    @Query("""
    SELECT new com.money.app.util.common.dto.AbcStatDto(ms.abc, SUM(ms.amount))
    FROM MonthlyStat ms
    WHERE ms.user = :user
      AND ms.year = :year
      AND ms.month = :month
      AND ms.type = :type
      AND ms.category IS NULL
      AND ms.abc IS NOT NULL
    GROUP BY ms.abc
""")
    List<AbcStatDto> findAbcStats(User user, int year, int month, TxType type);

    @Query("""
    SELECT new com.money.app.util.common.dto.SumStatDto(ms.type, ms.amount)
    FROM MonthlyStat ms
    WHERE ms.user = :user
      AND ms.year = :year
      AND ms.month = :month
      AND ms.category IS NULL
      AND ms.abc IS NULL
""")
    List<SumStatDto> findSumStats(User user, int year, int month);

    @Query("""
    SELECT new com.money.app.util.common.dto.CategoryStatDto(ms.category.id, ms.category.name, ms.amount)
    FROM MonthlyStat ms
    WHERE ms.user = :user
      AND ms.year = :year
      AND ms.month = :month
      AND ms.category IS NOT NULL
      AND ms.abc IS NULL
      ORDER BY ms.amount DESC
""")
    List<CategoryStatDto> findCategoryStats(User user, int year, int month);

    @Query("""
    SELECT new com.money.app.util.common.dto.CateAbcStatDto(ms.category.id, ms.category.name, ms.abc, ms.amount)
    FROM MonthlyStat ms
    WHERE ms.user = :user
      AND ms.year = :year
      AND ms.month = :month
      AND ms.category IS NOT NULL
      AND ms.abc IS NOT NULL
      ORDER BY ms.amount DESC, ms.abc ASC
""")
    List<CateAbcStatDto> findCateAbcStats(User user, int year, int month);

    @Query("""
    SELECT new com.money.app.util.common.dto.CateAbcStatDto(ms.category.id, ms.category.name, ms.abc, ms.amount)
    FROM MonthlyStat ms
    WHERE ms.user = :user
      AND ms.year = :year
      AND ms.month = :month
      AND ms.category IS NOT NULL
      AND ms.abc =:abc
      ORDER BY ms.amount DESC, ms.abc ASC
""")
    List<CateAbcStatDto> findCateAbcStatsWithAbc(User user, int year, int month, AbcType abc);

    @Query("""
    SELECT new com.money.app.util.common.dto.CateAbcStatDto(ms.category.id, ms.category.name, ms.abc, ms.amount)
    FROM MonthlyStat ms
    WHERE ms.user = :user
      AND ms.year = :year
      AND ms.month = :month
      AND ms.category = :category
      AND ms.abc =:abc
      ORDER BY ms.amount DESC, ms.abc ASC
""")
    CateAbcStatDto findCateAbcStatsWithCategoryAndAbc(User user, int year, int month, AbcType abc, Category category);
}

