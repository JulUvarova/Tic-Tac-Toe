package com.school21.Tic_Tac_Toe.web.mapper;

import com.school21.Tic_Tac_Toe.domain.model.stats.UserStatsModel;
import com.school21.Tic_Tac_Toe.web.model.UserStatsDtoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StatsWebMapper {
    public static UserStatsDtoResponse toStatsDto(UserStatsModel stats) {
        if (stats == null) return null;

        UserStatsDtoResponse statsDto = new UserStatsDtoResponse();
        statsDto.setUserId(stats.getUserId());
        statsDto.setWins(stats.getWins());
        statsDto.setLosses(stats.getLosses());
        statsDto.setDraws(stats.getDraws());
        statsDto.setWinRatio(stats.getWinRatio());
        return statsDto;
    }
}
