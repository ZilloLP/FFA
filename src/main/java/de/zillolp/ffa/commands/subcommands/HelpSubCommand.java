package de.zillolp.ffa.commands.subcommands;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.LanguageConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpSubCommand extends SubCommand {
    public HelpSubCommand(FFA plugin, String mainCommand, String... subCommands) {
        super(plugin, mainCommand, subCommands);
    }

    public boolean onCommand(FFA plugin, CommandSender sender, Command command, String[] args) {
        LanguageConfig languageConfig = plugin.getLanguageConfig();
        for (String message : languageConfig.getTranslatedLanguages("HELP_INFO")) {
            sender.sendMessage(message);
        }
        if (!(plugin.getPermissionsConfig().hasPermission((Player) sender, "ADMIN_PERMISSION"))) {
            return true;
        }
        for (String message : languageConfig.getTranslatedLanguages("ADMIN_HELP_INFO")) {
            sender.sendMessage(message);
        }
        return true;

    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return true;
    }
}
