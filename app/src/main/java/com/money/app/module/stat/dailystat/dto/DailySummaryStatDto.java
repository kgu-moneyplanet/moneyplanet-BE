package com.money.app.module.stat.dailystat.dto;

import com.money.app.util.common.dto.AbcStatResponseDto;
import com.money.app.util.common.dto.CateAbcResoponseDto;
import com.money.app.util.common.dto.CategoryStatResponseDto;
import com.money.app.util.common.dto.SumStatResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailySummaryStatDto {
    private List<DailySumStatListResponseDto> sumStatList;
    private SumStatResponseDto sumStat;
    private CategoryStatResponseDto categoryStat;
    private AbcStatResponseDto abcStat;
    private CateAbcResoponseDto cateAbcStat;
}
