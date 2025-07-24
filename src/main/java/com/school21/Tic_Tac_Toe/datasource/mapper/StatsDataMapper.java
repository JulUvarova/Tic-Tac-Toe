package com.school21.Tic_Tac_Toe.datasource.mapper;

import com.school21.Tic_Tac_Toe.datasource.model.UserRatioProjection;
import com.school21.Tic_Tac_Toe.datasource.model.UserStatsProjection;
import com.school21.Tic_Tac_Toe.domain.model.stats.UserRatioModel;
import com.school21.Tic_Tac_Toe.domain.model.stats.UserStatsModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StatsDataMapper {
    public static UserRatioModel toRatioModel(UserRatioProjection ratio) {
        if (ratio == null) {
            return null;
        }

        UserRatioModel ratioModel = new UserRatioModel();
        ratioModel.setUserId(ratio.getUserId());
        ratioModel.setWinRatio(ratio.getWinRatio());
        return ratioModel;
    }

    public static UserStatsModel toStatsModel(UserStatsProjection stats) {
        if (stats == null) {
            return null;
        }

        UserStatsModel statsModel = new UserStatsModel();
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
