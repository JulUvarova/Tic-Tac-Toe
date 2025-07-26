package com.school21.Tic_Tac_Toe.datasource.mapper;

import com.school21.Tic_Tac_Toe.datasource.model.UserRatioProjection;
import com.school21.Tic_Tac_Toe.datasource.model.UserStatsProjection;
import com.school21.Tic_Tac_Toe.domain.model.stats.UserRatio;
import com.school21.Tic_Tac_Toe.domain.model.stats.UserStats;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class StatsDataMapper {
    public static UserRatio toRatioModel(UserRatioProjection ratio) {
        if (ratio == null) {
            return null;
        }

        UserRatio ratioModel = new UserRatio();
        ratioModel.setUserId(UUID.fromString(ratio.getUserId()));
        ratioModel.setWinRatio(ratio.getWinRatio());
        return ratioModel;
    }

    public static UserStats toStatsModel(UserStatsProjection stats) {
        if (stats == null) {
            return null;
        }

        UserStats statsModel = new UserStats();
        statsModel.setUserId(stats.getUserId());
        statsModel.setWins(stats.getWins());
        statsModel.setLosses(stats.getLosses());
        statsModel.setDraws(stats.getDraws());
        statsModel.setWinRatio(countWinRatio(stats));

        return statsModel;
    }

    private float countWinRatio(UserStatsProjection stats) {
        int total = stats.getWins() + stats.getLosses() + stats.getDraws();
        if (total == 0) {
            return 0.0f;
        }
        return stats.getWins() / (total * 1.0f);
    }
}
