package de.zillolp.ffa.placeholder;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.LanguageConfig;
import de.zillolp.ffa.map.MapManager;
import de.zillolp.ffa.profiles.PlayerProfile;
import de.zillolp.ffa.ranking.rankings.TopRanking;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaceholderListener extends PlaceholderExpansion {
    private final FFA plugin;

    public PlaceholderListener(FFA plugin) {
        this.plugin = plugin;
        register();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "ZilloLP";
    }

    @Override
    public String getIdentifier() {
        return "ffa";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        UUID uuid = player.getUniqueId();
        PlayerProfile playerProfile = plugin.getPlayerManager().getPlayerProfiles().get(uuid);
        if (playerProfile == null) {
            return "N/A";
        }
        LanguageConfig languageConfig = plugin.getLanguageConfig();
        switch (identifier.toUpperCase()) {
            case "KILLS":
                // %ffa_kills%
                return languageConfig.formatNumber(playerProfile.getKills());
            case "DEATHS":
                // %ffa_deaths%
                return languageConfig.formatNumber(playerProfile.getDeaths());
            case "KILLSTREAK":
                // %ffa_killstreak%
                return languageConfig.formatNumber(playerProfile.getKillStreak());
            case "MAP":
                // %ffa_map%
                MapManager mapManager = plugin.getMapManager();
                HashMap<UUID, de.zillolp.ffa.map.Map> playerMaps = mapManager.getPlayerMaps();
                if (!(playerMaps.containsKey(uuid))) {
                    return "N/A";
                }
                return playerMaps.get(uuid).getMapName();
            case "PLACE":
                // %ffa_place%
                TopRanking topRanking = plugin.getRankingManager().getTopRanking();
                if (!(topRanking.isRunning())) {
                    return "N/A";
                }
                long index = 0;
                for (Map.Entry<String, Long> rankEntry : topRanking.getSortedData().entrySet()) {
                    index++;
                    if (rankEntry.getKey().equalsIgnoreCase(playerProfile.getName())) {
                        break;
                    }
                }
                return languageConfig.formatNumber(index);
            default:
                return "N/A";
        }
    }
}
