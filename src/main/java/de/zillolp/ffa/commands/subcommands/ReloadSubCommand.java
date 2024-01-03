package de.zillolp.ffa.commands.subcommands;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.hologram.HologramManager;
import de.zillolp.ffa.hologram.holograms.TextHologram;
import de.zillolp.ffa.manager.ScoreboardManager;
import de.zillolp.ffa.map.Map;
import de.zillolp.ffa.map.MapManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ReloadSubCommand extends SubCommand {

    public ReloadSubCommand(FFA plugin, String mainCommand, String... subCommands) {
        super(plugin, mainCommand, subCommands);
    }

    public boolean onCommand(FFA plugin, CommandSender sender, Command command, String[] args) {
        plugin.getConfigManager().reloadConfigs();
        MapManager mapManager = plugin.getMapManager();
        HologramManager hologramManager = plugin.getHologramManager();
        HashMap<UUID, Map> playerMaps = mapManager.getPlayerMaps();
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            hologramManager.deleteHolograms(player);
            if (!(playerMaps.containsKey(uuid))) {
                continue;
            }
            Location location = playerMaps.get(uuid).getTopLocation();
            if (location == null) {
                continue;
            }
            hologramManager.spawnHologram(player, new TextHologram(plugin, plugin.getRankingManager().getTopRanking().getLines()), location);
        }
        ScoreboardManager scoreboardManager = plugin.getScoreboardManager();
        for (Player player : Bukkit.getOnlinePlayers()) {
            scoreboardManager.delete(player);
            scoreboardManager.createScoreboards(player);
        }
        mapManager.loadMaps();
        JoinSubCommand joinSubCommand = plugin.getJoinSubCommand();
        joinSubCommand.setSubCommands(plugin.getMapSubCommands(joinSubCommand));
        sender.sendMessage(plugin.getLanguageConfig().getTranslatedLanguage("PREFIX") + "§7The §bsettings §7have been reloaded.");
        return true;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return plugin.getPermissionsConfig().hasPermission((Player) sender, "ADMIN_PERMISSION");
    }
}
