package de.zillolp.ffa.commands.subcommands;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.LanguageConfig;
import de.zillolp.ffa.hologram.HologramManager;
import de.zillolp.ffa.hologram.holograms.TextHologram;
import de.zillolp.ffa.map.Map;
import de.zillolp.ffa.map.MapManager;
import de.zillolp.ffa.profiles.PlayerProfile;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.UUID;

public class JoinSubCommand extends SubCommand {
    private final LanguageConfig languageConfig;
    private final MapManager mapManager;

    public JoinSubCommand(FFA plugin, String mainCommand, String... subCommands) {
        super(plugin, mainCommand, subCommands);
        this.languageConfig = plugin.getLanguageConfig();
        this.mapManager = plugin.getMapManager();
    }

    public boolean onCommand(FFA plugin, CommandSender sender, Command command, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            Map currentMap = mapManager.getCurrentMap();
            if (currentMap == null) {
                sender.sendMessage(languageConfig.getLanguageWithPrefix("NO_CURRENT_MAP"));
                return true;
            }
            sendPlayerToMap(player, currentMap);
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(languageConfig.getLanguageWithPrefix("UNKNOWN_COMMAND"));
            return true;
        }
        String mapName = args[1];
        HashMap<String, Map> maps = mapManager.getMaps();
        LanguageConfig languageConfig = plugin.getLanguageConfig();
        if (!(maps.containsKey(mapName))) {
            sender.sendMessage(languageConfig.getLanguageWithPrefix("MAP_NOT_EXISTS"));
            return true;
        }
        Map map = maps.get(mapName);
        if (!(map.isPlayable())) {
            sender.sendMessage(languageConfig.getLanguageWithPrefix("MAP_NOT_PLAYABLE"));
            return true;
        }
        sendPlayerToMap(player, map);
        return true;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return true;
    }

    private void sendPlayerToMap(Player player, Map map) {
        UUID uuid = player.getUniqueId();
        HashMap<UUID, PlayerProfile> playerProfiles = plugin.getPlayerManager().getPlayerProfiles();
        if (!(playerProfiles.containsKey(uuid))) {
            return;
        }
        PlayerInventory playerInventory = player.getInventory();
        HashMap<UUID, Map> playerMaps = mapManager.getPlayerMaps();
        if (!(playerMaps.containsKey(uuid))) {
            PlayerProfile playerProfile = playerProfiles.get(uuid);
            playerProfile.setLastLocation(player.getLocation());
            playerProfile.setItems(playerInventory.getContents());
            playerProfile.setArmor(playerInventory.getArmorContents());
        } else if (playerMaps.get(uuid) == map) {
            player.sendMessage(languageConfig.getLanguageWithPrefix("ALREADY_JOINED"));
            return;
        }
        playerMaps.put(uuid, map);
        playerInventory.setContents(map.getItems());
        playerInventory.setArmorContents(map.getArmor());
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(map.getSpawnLocation());
        Location topLocation = map.getTopLocation();
        if (topLocation != null) {
            HologramManager hologramManager = plugin.getHologramManager();
            hologramManager.deleteHologram(player, topLocation);
            hologramManager.spawnHologram(player, new TextHologram(plugin, plugin.getRankingManager().getTopRanking().getLines()), topLocation);
        }
        plugin.getScoreboardManager().createScoreboards(player);
        player.sendMessage(languageConfig.getLanguageWithPrefix("MAP_JOIN").replace("%map%", map.getMapName()).replace("%builder%", map.getBuilderName()));
    }
}
