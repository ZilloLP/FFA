package de.zillolp.ffa.timer;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.LanguageConfig;
import de.zillolp.ffa.config.customconfigs.PluginConfig;
import de.zillolp.ffa.map.Map;
import de.zillolp.ffa.map.MapManager;
import de.zillolp.ffa.profiles.PlayerProfile;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MapUpdater extends CustomUpdater {
    private final PluginConfig pluginConfig;
    private final MapManager mapManager;
    private final int defaultSeconds;
    private int currentSeconds;

    public MapUpdater(FFA plugin) {
        super(plugin, 20);
        this.pluginConfig = plugin.getPluginConfig();
        this.mapManager = plugin.getMapManager();
        this.defaultSeconds = plugin.getPluginConfig().getFileConfiguration().getInt("Map change minutes", 15) * 60;
        this.currentSeconds = defaultSeconds;
    }

    @Override
    protected void tick() {
        ArrayList<Map> playableMaps = mapManager.getPlayableMaps();
        if (playableMaps.isEmpty()) {
            return;
        }
        Map currentMap = mapManager.getCurrentMap();
        if (currentMap == null) {
            mapManager.setRandomMap();
            return;
        }
        currentSeconds--;
        World world = Bukkit.getWorld(currentMap.getMapName());
        if (world == null) {
            mapManager.setRandomMap();
            return;
        }
        boolean hasActionbar = pluginConfig.getFileConfiguration().getBoolean("Actionbar", true);
        HashMap<UUID, Map> playerMaps = mapManager.getPlayerMaps();
        LanguageConfig languageConfig = plugin.getLanguageConfig();
        for (Player player : world.getPlayers()) {
            if (!(playerMaps.containsKey(player.getUniqueId()))) {
                continue;
            }
            UUID uuid = player.getUniqueId();
            HashMap<UUID, PlayerProfile> playerProfiles = plugin.getPlayerManager().getPlayerProfiles();
            if ((!(playerProfiles.containsKey(uuid))) || (!(hasActionbar))) {
                continue;
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(languageConfig.getReplacedLanguage("MAP_CHANGE_COUNTER", playerProfiles.get(uuid))));
        }
        if (currentSeconds != 0) {
            return;
        }
        mapManager.setRandomMap();
        for (Player player : world.getPlayers()) {
            UUID uuid = player.getUniqueId();
            if (!(playerMaps.containsKey(uuid))) {
                continue;
            }
            if (hasActionbar) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(languageConfig.getTranslatedLanguage("MAP_CHANGE")));
            }
            plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
                @Override
                public void run() {
                    player.performCommand("ffa join");
                }
            });
        }
        currentSeconds = defaultSeconds;
    }

    public int getCurrentSeconds() {
        return currentSeconds;
    }
}
