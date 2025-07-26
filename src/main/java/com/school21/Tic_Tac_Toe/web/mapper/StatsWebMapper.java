package com.school21.Tic_Tac_Toe.web.mapper;

import com.school21.Tic_Tac_Toe.domain.model.stats.UserRatio;
import com.school21.Tic_Tac_Toe.domain.model.stats.UserStats;
import com.school21.Tic_Tac_Toe.web.model.stats.UserRatioDtoResponse;
import com.school21.Tic_Tac_Toe.web.model.stats.UserStatsDtoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StatsWebMapper {
    public static UserStatsDtoResponse toStatsDto(UserStats stats) {
        if (stats == null) return null;

        UserStatsDtoResponse statsDto = new UserStatsDtoResponse();
        statsDto.setUserId(stats.getUserId());
        statsDto.setWins(stats.getWins());
        statsDto.setLosses(stats.getLosses());
        statsDto.setDraws(stats.getDraws());
        statsDto.setWinRatio(stats.getWinRatio());
        return statsDto;
    }

    public static UserRatioDtoResponse toRatioDto(UserRatio ratio) {
        if (ratio == null) return null;

        UserRatioDtoResponse ratioDto = new UserRatioDtoResponse();
        ratioDto.setUserId(ratio.getUserId());
        ratioDto.setWinRatio(ratio.getWinRatio());
        return ratioDto;
    }
}
