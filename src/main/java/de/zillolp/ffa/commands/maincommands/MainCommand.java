package de.zillolp.ffa.commands.maincommands;

import de.zillolp.ffa.commands.subcommands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MainCommand implements TabExecutor {
    protected final HashMap<String, ArrayList<SubCommand>> subCommands;

    public MainCommand() {
        this.subCommands = new HashMap<>();
    }

    @Override
    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    public void registerSubCommand(SubCommand subCommand) {
        ArrayList<SubCommand> mainSubCommands;
        String mainCommand = subCommand.getMainCommand();
        if (subCommands.containsKey(mainCommand)) {
            mainSubCommands = subCommands.get(mainCommand);
        } else {
            mainSubCommands = new ArrayList<>();
            subCommands.put(mainCommand, mainSubCommands);
        }
        mainSubCommands.add(subCommand);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        int length = args.length;
        ArrayList<String> tabCommands = new ArrayList<>();
        for (Map.Entry<String, ArrayList<SubCommand>> mainSubCommands : subCommands.entrySet()) {
            for (SubCommand subCommand : mainSubCommands.getValue()) {
                if (!(subCommand.hasPermission(sender))) {
                    continue;
                }
                tabCommands.remove(subCommand.getMainCommand());
                if (length > 1) {
                    tabCommands.addAll(getPossibleTabCommands(args[length - 1], subCommand.getTabCommands(args[0], args[length - 2], length - 1)));
                    continue;
                }
                String mainCommand = subCommand.getMainCommand();
                if (!(mainCommand.toLowerCase().startsWith(args[0].toLowerCase()))) {
                    continue;
                }
                tabCommands.add(mainCommand);
            }
        }
        if (!(tabCommands.isEmpty())) {
            return tabCommands;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (sender == player) {
                continue;
            }
            String playerName = player.getName();
            if (length == 0) {
                tabCommands.add(playerName);
                continue;
            }
            String input = args[length - 1];
            if (input.isEmpty()) {
                tabCommands.add(playerName);
                continue;
            }
            if (!(playerName.toLowerCase().startsWith(input.toLowerCase()))) {
                continue;
            }
            tabCommands.add(playerName);
        }
        return tabCommands;
    }

    private List<String> getPossibleTabCommands(String input, List<String> commands) {
        List<String> tabCommands = new ArrayList<>();
        for (String command : commands) {
            if (!(command.toLowerCase().startsWith(input.toLowerCase()))) {
                continue;
            }
            tabCommands.add(command);
        }
        return tabCommands;
    }
}
