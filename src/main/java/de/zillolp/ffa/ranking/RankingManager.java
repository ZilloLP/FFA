package de.zillolp.ffa.ranking;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.ranking.rankings.TopRanking;

public class RankingManager {
    private final TopRanking topRanking;

    public RankingManager(FFA plugin) {
        this.topRanking = new TopRanking(plugin);
    }

    public TopRanking getTopRanking() {
        return topRanking;
    }
}
