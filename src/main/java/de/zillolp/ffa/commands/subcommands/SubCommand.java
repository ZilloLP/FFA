package de.zillolp.ffa.commands.subcommands;

import de.zillolp.ffa.FFA;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SubCommand {
    protected final FFA plugin;
    private final String mainCommand;
    private String[] subCommands;

    public SubCommand(FFA plugin, String mainCommand, String... subCommands) {
        this.plugin = plugin;
        this.mainCommand = mainCommand;
        this.subCommands = subCommands;
    }

    public abstract boolean onCommand(FFA cookieClicker, CommandSender commandSender, Command command, String[] args);

    public boolean isNumeric(String value) {
        try {
            int number = Integer.parseInt(value);
            return number > 0;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    public abstract boolean hasPermission(CommandSender sender);

    public void setSubCommands(String[] subCommands) {
        this.subCommands = subCommands;
    }

    public String getMainCommand() {
        return mainCommand;
    }

    public List<String> getTabCommands(String subCommand, String command, int position) {
        if (subCommands.length < position) {
            return new ArrayList<>();
        }
        position--;
        List<String> tabCommands = Arrays.asList(subCommands[position].split(";"));
        if (position > 0) {
            position--;
        } else if (mainCommand.equalsIgnoreCase(command)) {
            return tabCommands;
        }
        for (String replacedCommand : subCommands[position].split(";")) {
            if (replacedCommand.equalsIgnoreCase(command) && subCommand.equalsIgnoreCase(mainCommand)) {
                return tabCommands;
            }
        }
        return new ArrayList<>();
    }
}
