package de.zillolp.ffa.map;

import de.zillolp.ffa.FFA;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class MapManager {
    private final FFA plugin;
    private final ArrayList<Map> playableMaps;
    private final HashMap<UUID, Map> playerMaps;
    private final HashMap<String, Map> maps;
    private final Random random;
    private Map currentMap;

    public MapManager(FFA plugin) {
        this.plugin = plugin;
        this.playableMaps = new ArrayList<>();
        this.playerMaps = new HashMap<>();
        this.maps = new HashMap<>();
        this.random = new Random();
        loadMaps();
        setRandomMap();
    }

    public void loadMaps() {
        maps.clear();
        playableMaps.clear();
        String path = "locations.Maps";
        FileConfiguration fileConfiguration = plugin.getLocationConfig().getFileConfiguration();
        if (!(fileConfiguration.isConfigurationSection(path))) {
            return;
        }
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection(path);
        if (configurationSection == null) {
            return;
        }
        for (String mapName : configurationSection.getKeys(false)) {
            Map map = new Map(plugin, mapName);
            map.load();
            maps.put(mapName, map);
            if (!(map.isPlayable())) {
                continue;
            }
            playableMaps.add(map);
        }
    }

    public void setRandomMap() {
        if (playableMaps.isEmpty()) {
            return;
        }
        if (playableMaps.size() == 1) {
            currentMap = playableMaps.get(0);
            return;
        }
        Map map;
        do {
            map = playableMaps.get(random.nextInt(playableMaps.size()));
        } while (currentMap == map);
        this.currentMap = map;
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public ArrayList<Map> getPlayableMaps() {
        return playableMaps;
    }

    public HashMap<UUID, Map> getPlayerMaps() {
        return playerMaps;
    }

    public HashMap<String, Map> getMaps() {
        return maps;
    }
}
