package de.zillolp.ffa.timer;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.manager.ScoreboardManager;
import de.zillolp.ffa.map.Map;
import de.zillolp.ffa.map.MapManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ScoreboardUpdater extends CustomUpdater {
    private final MapManager mapManager;
    private final ScoreboardManager scoreboardManager;

    public ScoreboardUpdater(FFA plugin) {
        super(plugin, 1);
        this.mapManager = plugin.getMapManager();
        this.scoreboardManager = plugin.getScoreboardManager();
    }

    @Override
    protected void tick() {
        HashMap<UUID, Map> playerMaps = mapManager.getPlayerMaps();
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            if (!(playerMaps.containsKey(uuid))) {
                continue;
            }
            FileConfiguration fileConfiguration = plugin.getPluginConfig().getFileConfiguration();
            if (fileConfiguration.getBoolean("Scoreboard", true)) {
                if (scoreboardManager.hasScoreboard(player.getUniqueId())) {
                    scoreboardManager.updateSideBoard(player);
                } else {
                    scoreboardManager.createSideBoard(player);
                }
            }
            if (!(fileConfiguration.getBoolean("Hearts", true))) {
                return;
            }
            if (scoreboardManager.hasHealth(player.getUniqueId())) {
                scoreboardManager.updateHealthBoard(player);
                continue;
            }
            scoreboardManager.createHealthBoard(player);
        }
    }
}
