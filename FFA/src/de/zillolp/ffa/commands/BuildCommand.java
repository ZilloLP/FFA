package de.zillolp.ffa.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.LanguageTools;
import de.zillolp.ffa.config.tools.PermissionTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.profiles.PlayerProfil;

public class BuildCommand implements CommandExecutor {
	private HashMap<String, ItemStack[]> inv = new HashMap<String, ItemStack[]>();
	private HashMap<String, ItemStack[]> armor = new HashMap<String, ItemStack[]>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String PREFIX = LanguageTools.getPREFIX();
		boolean english = ConfigTools.getEnglish();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(PermissionTools.getADMIN_PERMISSION())) {
				if (args.length == 0) {
					PlayerProfil profil = Main.getInstance().playerprofiles.get(player);
					String name = player.getName();
					if (profil.getBuildmode()) {
						profil.setBuildmode(false);
						player.getInventory().setContents(inv.get(name));
						player.getInventory().setArmorContents(armor.get(name));
						inv.remove(name);
						armor.remove(name);
						player.setGameMode(ConfigTools.getGamemode());
						if (english) {
							player.sendMessage(PREFIX + "§eBuildmode §cdeactivated§7.");
						} else {
							player.sendMessage(PREFIX + "§eBuildModus §cdeaktiviert§7.");
						}
					} else {
						profil.setBuildmode(true);
						inv.put(name, player.getInventory().getContents());
						armor.put(name, player.getInventory().getArmorContents());
						player.setGameMode(GameMode.CREATIVE);
						if (english) {
							player.sendMessage(PREFIX + "§eBuildmode §aactivated§7.");
						} else {
							player.sendMessage(PREFIX + "§eBuildModus §aaktiviert§7.");
						}
					}
				} else if (args.length == 1) {
					Player k = Bukkit.getPlayer(args[0]);
					if (Bukkit.getOnlinePlayers().contains(k)) {
						PlayerProfil profil = Main.getInstance().playerprofiles.get(k);
						String name = k.getName();
						if (profil.getBuildmode()) {
							if (english) {
								player.sendMessage(PREFIX + "§7The player §6" + name + " §7was set out of §eBuildmode§7.");
							} else {
								player.sendMessage(PREFIX + "§7Der Spieler §6" + name
										+ " §7wurde aus dem §eBuildModus §7gesetzt.");
							}
							profil.setBuildmode(false);
							k.getInventory().setContents(inv.get(name));
							k.getInventory().setArmorContents(armor.get(name));
							inv.remove(name);
							armor.remove(name);
							k.setGameMode(GameMode.SURVIVAL);
							if (english) {
								k.sendMessage(PREFIX + "§eBuildmode §cdeactivated§7.");
							} else {
								k.sendMessage(PREFIX + "§eBuildModus §cdeaktiviert§7.");
							}
						} else {
							if (english) {
								player.sendMessage(PREFIX + "§7The player §6" + name + " §7was set to §eBuildmode§7.");
							} else {
								player.sendMessage(
										PREFIX + "§7Der Spieler §6" + name + " §7wurde in den §eBuildModus §7gesetzt.");
							}
							profil.setBuildmode(true);
							inv.put(name, k.getInventory().getContents());
							armor.put(name, k.getInventory().getArmorContents());
							k.setGameMode(GameMode.CREATIVE);
							if (english) {
								k.sendMessage(PREFIX + "§eBuildmode §aactivated§7.");
							} else {
								k.sendMessage(PREFIX + "§eBuildModus §aaktiviert§7.");
							}
						}
					} else {
						player.sendMessage(PREFIX + "§cDieser Spieler konnte nicht gefunden werden!");
					}
				}
			} else {
				player.sendMessage(PREFIX + LanguageTools.getNO_PERMISSION());
			}
		} else {
			Bukkit.getConsoleSender().sendMessage(PREFIX + LanguageTools.getONLY_PLAYER());
		}
		return false;
	}
}
