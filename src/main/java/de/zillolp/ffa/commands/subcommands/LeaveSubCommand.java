package de.zillolp.ffa.commands.subcommands;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.LanguageConfig;
import de.zillolp.ffa.map.Map;
import de.zillolp.ffa.profiles.PlayerProfile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class LeaveSubCommand extends SubCommand {
    private final LanguageConfig languageConfig;

    public LeaveSubCommand(FFA plugin, String mainCommand, String... subCommands) {
        super(plugin, mainCommand, subCommands);
        this.languageConfig = plugin.getLanguageConfig();
    }

    public boolean onCommand(FFA plugin, CommandSender sender, Command command, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(languageConfig.getLanguageWithPrefix("UNKNOWN_COMMAND"));
            return true;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        HashMap<UUID, Map> playerMaps = plugin.getMapManager().getPlayerMaps();
        if (!(playerMaps.containsKey(uuid))) {
            sender.sendMessage(languageConfig.getLanguageWithPrefix("NOT_IN_GAME"));
            return true;
        }
        HashMap<UUID, PlayerProfile> playerProfiles = plugin.getPlayerManager().getPlayerProfiles();
        if (!(playerProfiles.containsKey(uuid))) {
            plugin.getLogger().log(Level.WARNING, "The PlayerProfile is null when leaving");
            return true;
        }
        Map map = playerMaps.get(uuid);
        player.sendMessage(languageConfig.getLanguageWithPrefix("MAP_LEAVE").replace("%map%", map.getMapName()).replace("%builder%", map.getBuilderName()));
        playerProfiles.get(uuid).resetPlayer(player);
        playerMaps.remove(uuid);
        plugin.getScoreboardManager().delete(player);
        return true;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return true;
    }
}
