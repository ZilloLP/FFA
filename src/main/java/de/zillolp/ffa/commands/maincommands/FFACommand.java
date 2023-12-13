package de.zillolp.ffa.commands.maincommands;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.commands.subcommands.SubCommand;
import de.zillolp.ffa.config.customconfigs.LanguageConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class FFACommand extends MainCommand {
    private final FFA plugin;

    public FFACommand(FFA plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LanguageConfig languageConfig = plugin.getLanguageConfig();
        if (!(plugin.getDatabaseConnector().hasConnection())) {
            sender.sendMessage(languageConfig.getLanguageWithPrefix("NO_DATABASE_CONNECTION"));
            return true;
        }
        if (args.length == 0) {
            PluginDescriptionFile pluginDescription = plugin.getDescription();
            sender.sendMessage("§c§lPlugin info:");
            sender.sendMessage("§7Plugin Name: §e" + pluginDescription.getName());
            sender.sendMessage("§7Plugin Version: §e" + pluginDescription.getVersion());
            sender.sendMessage("§7Author: §eZilloLP");
            sender.sendMessage("§7Discord: §ehttps://discord.gg/NBs27JK");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(languageConfig.getLanguageWithPrefix("ONLY_PLAYER"));
            return true;
        }
        if (!(subCommands.containsKey(args[0].toLowerCase()))) {
            sender.sendMessage(languageConfig.getLanguageWithPrefix("UNKNOWN_COMMAND"));
            return true;
        }
        SubCommand subCommand = subCommands.get(args[0]).get(0);
        if (!(subCommand.hasPermission(sender))) {
            sender.sendMessage(languageConfig.getLanguageWithPrefix("NO_PERMISSION"));
            return true;
        }
        return subCommand.onCommand(plugin, sender, command, args);
    }
}
