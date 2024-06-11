package de.zillolp.ffa.ranking.rankings;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.LanguageConfig;
import de.zillolp.ffa.hologram.HologramManager;
import de.zillolp.ffa.hologram.holograms.Hologram;
import de.zillolp.ffa.hologram.holograms.TextHologram;
import de.zillolp.ffa.profiles.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class TopRanking extends Ranking {
    private final LanguageConfig languageConfig;

    public TopRanking(FFA plugin) {
        super(plugin);
        this.languageConfig = plugin.getLanguageConfig();
        start();
    }

    @Override
    public void rankData() {
        sortedData.clear();
        for (PlayerProfile playerProfile : playerManager.getPlayerProfiles().values()) {
            cachedData.put(playerProfile.getName(), playerProfile.getKills());
        }
        cachedData.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(playerData -> sortedData.put(playerData.getKey(), playerData.getValue()));
    }

    @Override
    public void setRanking() {
        HologramManager hologramManager = plugin.getHologramManager();
        HashMap<UUID, HashMap<Hologram, Location>> hologramLists = hologramManager.getHologramLists();
        if (hologramLists == null) {
            return;
        }
        String[] lines = getLines();
        for (Map.Entry<UUID, de.zillolp.ffa.map.Map> mapEntry : plugin.getMapManager().getPlayerMaps().entrySet()) {
            UUID uuid = mapEntry.getKey();
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                continue;
            }
            de.zillolp.ffa.map.Map map = mapEntry.getValue();
            if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
                continue;
            }
            Location topLocation = map.getTopLocation();
            if (topLocation == null) {
                continue;
            }
            if (hologramLists.containsKey(uuid) && hologramLists.get(uuid).containsValue(topLocation)) {
                ((TextHologram) hologramManager.getHologram(player, topLocation)).changeLines(player, lines);
                continue;
            }
            hologramManager.spawnHologram(player, new TextHologram(plugin, lines), topLocation);
        }
    }

    public String[] getLines() {
        LinkedList<String> lines = new LinkedList<>();
        lines.add(languageConfig.getTranslatedLanguage("Top-10.HEADER"));
        if (!(isRunning())) {
            return lines.toArray(new String[0]);
        }
        int index = 0;
        for (Map.Entry<String, Long> rankEntry : sortedData.entrySet()) {
            index++;
            if (index > 10) {
                break;
            }
            long kills = 0;
            String playerName = rankEntry.getKey();
            UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
            HashMap<UUID, PlayerProfile> playerProfiles = playerManager.getPlayerProfiles();
            if (playerProfiles.containsKey(uuid)) {
                kills = playerProfiles.get(uuid).getKills();
            }
            lines.add(languageConfig.getTranslatedLanguage("Top-10.PLACE").replace("%kills%", languageConfig.formatNumber(kills)).replace("%place%", languageConfig.formatNumber(index)).replace("%player%", playerName));
        }
        return lines.toArray(new String[0]);
    }
}
