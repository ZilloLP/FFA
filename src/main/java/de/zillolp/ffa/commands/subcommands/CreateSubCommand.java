package de.zillolp.ffa.commands.subcommands;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.LanguageConfig;
import de.zillolp.ffa.map.Map;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateSubCommand extends SubCommand {
    public CreateSubCommand(FFA plugin, String mainCommand, String... subCommands) {
        super(plugin, mainCommand, subCommands);
    }

    public boolean onCommand(FFA plugin, CommandSender sender, Command command, String[] args) {
        LanguageConfig languageConfig = plugin.getLanguageConfig();
        if (args.length != 3) {
            sender.sendMessage(languageConfig.getLanguageWithPrefix("UNKNOWN_COMMAND"));
            return true;
        }
        String mapName = args[1];
        World world = Bukkit.getWorld(mapName);
        String PREFIX = languageConfig.getTranslatedLanguage("PREFIX");
        if (!(Bukkit.getWorlds().contains(world))) {
            sender.sendMessage(PREFIX + "§cThere is no world with this map name.");
            return true;
        }
        String builderName = args[2];
        Map map = new Map(plugin, mapName);
        if (map.isExisting()) {
            sender.sendMessage(PREFIX + "§cThis map already exists.");
            return true;
        }
        map.create(builderName);
        SetSubCommand setSubCommand = plugin.getSetSubCommand();
        setSubCommand.setSubCommands(plugin.getMapSubCommands(setSubCommand));
        sender.sendMessage(PREFIX + "§7You have created the map §e" + mapName + "§7.");
        return true;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return plugin.getPermissionsConfig().hasPermission((Player) sender, "ADMIN_PERMISSION");
    }
}
