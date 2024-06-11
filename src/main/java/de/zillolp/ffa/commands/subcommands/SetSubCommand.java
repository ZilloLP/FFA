package de.zillolp.ffa.commands.subcommands;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.LanguageConfig;
import de.zillolp.ffa.hologram.HologramManager;
import de.zillolp.ffa.hologram.holograms.TextHologram;
import de.zillolp.ffa.map.Map;
import de.zillolp.ffa.map.MapManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SetSubCommand extends SubCommand {
    private final MapManager mapManager;

    public SetSubCommand(FFA plugin, String mainCommand, String... subCommands) {
        super(plugin, mainCommand, subCommands);
        this.mapManager = plugin.getMapManager();
    }

    public boolean onCommand(FFA plugin, CommandSender sender, Command command, String[] args) {
        int length = args.length;
        LanguageConfig languageConfig = plugin.getLanguageConfig();
        if (length < 3 || length > 4) {
            sender.sendMessage(languageConfig.getLanguageWithPrefix("UNKNOWN_COMMAND"));
            return true;
        }
        String mapName = args[1];
        HashMap<String, Map> maps = mapManager.getMaps();
        String PREFIX = languageConfig.getTranslatedLanguage("PREFIX");
        if (!(maps.containsKey(mapName))) {
            sender.sendMessage(PREFIX + "§cThis map does not exist.");
            return true;
        }
        String type = args[2];
        Map map = maps.get(mapName);
        Player player = (Player) sender;
        if (length == 4) {
            if (!(isNumeric(args[3]))) {
                sender.sendMessage(PREFIX + "§cYour input §4" + args[3] + " §cis not a number!");
                return true;
            }
            map.setRadius(Integer.parseInt(args[3]));
            sender.sendMessage(PREFIX + "§7You have set the §cradius §7for the map §e" + mapName + " §7to §c" + args[3] + "§7.");
            return true;
        }
        if (type.equalsIgnoreCase("Spawn")) {
            map.setSpawnLocation(player.getLocation());
            checkPlayable(map);
            sender.sendMessage(PREFIX + "§7You have set the §6spawn §7for the map §e" + mapName + "§7.");
            return true;
        }
        if (type.equalsIgnoreCase("Top")) {
            World world = Bukkit.getWorld(map.getMapName());
            if (world == null) {
                return true;
            }
            Location location = player.getLocation();
            Location topLocation = map.getTopLocation();
            HologramManager hologramManager = plugin.getHologramManager();
            HashMap<UUID, Map> playerMaps = mapManager.getPlayerMaps();
            if (topLocation != null) {
                for (Player player1 : world.getPlayers()) {
                    if (!(playerMaps.containsKey(player1.getUniqueId()))) {
                        continue;
                    }
                    hologramManager.deleteHologram(player1, topLocation);
                }
            }
            map.setTopLocation(location);
            for (Player player1 : world.getPlayers()) {
                if (!(playerMaps.containsKey(player1.getUniqueId()))) {
                    continue;
                }
                hologramManager.spawnHologram(player1, new TextHologram(plugin, plugin.getRankingManager().getTopRanking().getLines()), location);
            }
            sender.sendMessage(PREFIX + "§7You have set the §btop-10 §7for the map §e" + mapName + "§7.");
            return true;
        }
        if (type.equalsIgnoreCase("Protection")) {
            if (map.isCuboid()) {
                map.setCuboid(false);
                sender.sendMessage(PREFIX + "§7You have set the §5protection §7for the map §e" + mapName + " §7to §5circular§7.");
                return true;
            }
            map.setCuboid(true);
            sender.sendMessage(PREFIX + "§7You have set the §5protection §7for the map §e" + mapName + " §7to §5squared§7.");
            return true;
        }
        if (!(type.equalsIgnoreCase("Kit"))) {
            sender.sendMessage(languageConfig.getLanguageWithPrefix("UNKNOWN_COMMAND"));
            return true;
        }
        PlayerInventory playerInventory = player.getInventory();
        map.setItems(playerInventory.getContents());
        map.setArmor(playerInventory.getArmorContents());
        World world = Bukkit.getWorld(mapName);
        if (world != null) {
            for (Player player1 : world.getPlayers()) {
                PlayerInventory playerInventory1 = player1.getInventory();
                playerInventory1.setContents(map.getItems());
                playerInventory1.setArmorContents(map.getArmor());
            }
        }
        checkPlayable(map);
        sender.sendMessage(PREFIX + "§7You have set the §3kit §7for the map §e" + mapName + "§7.");
        return true;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return plugin.getPermissionsConfig().hasPermission((Player) sender, "ADMIN_PERMISSION");
    }

    private void checkPlayable(Map map) {
        ArrayList<Map> playableMaps = plugin.getMapManager().getPlayableMaps();
        if (map.isPlayable() && (!(playableMaps.contains(map)))) {
            playableMaps.add(map);
            JoinSubCommand joinSubCommand = plugin.getJoinSubCommand();
            joinSubCommand.setSubCommands(plugin.getMapSubCommands(joinSubCommand));
        }
    }
}
