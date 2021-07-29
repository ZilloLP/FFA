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
			Player p = (Player) sender;
			if (p.hasPermission(PermissionTools.getADMIN_PERMISSION())) {
				if (args.length == 0) {
					PlayerProfil profil = Main.playerprofiles.get(p);
					String name = p.getName();
					if (profil.getBuildmode()) {
						profil.setBuildmode(false);
						p.getInventory().setContents(inv.get(name));
						p.getInventory().setArmorContents(armor.get(name));
						inv.remove(name);
						armor.remove(name);
						p.setGameMode(ConfigTools.getGamemode());
						if (english) {
							p.sendMessage(PREFIX + "§eBuildmode §cdeactivated§7.");
						} else {
							p.sendMessage(PREFIX + "§eBuildModus §cdeaktiviert§7.");
						}
					} else {
						profil.setBuildmode(true);
						inv.put(name, p.getInventory().getContents());
						armor.put(name, p.getInventory().getArmorContents());
						p.setGameMode(GameMode.CREATIVE);
						if (english) {
							p.sendMessage(PREFIX + "§eBuildmode §aactivated§7.");
						} else {
							p.sendMessage(PREFIX + "§eBuildModus §aaktiviert§7.");
						}
					}
				} else if (args.length == 1) {
					Player k = Bukkit.getPlayer(args[0]);
					if (Bukkit.getOnlinePlayers().contains(k)) {
						PlayerProfil profil = Main.playerprofiles.get(k);
						String name = k.getName();
						if (profil.getBuildmode()) {
							if (english) {
								p.sendMessage(PREFIX + "§7The player §6" + name + " §7was set out of §eBuildmode§7.");
							} else {
								p.sendMessage(PREFIX + "§7Der Spieler §6" + name
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
								p.sendMessage(PREFIX + "§7The player §6" + name + " §7was set to §eBuildmode§7.");
							} else {
								p.sendMessage(
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
						p.sendMessage(PREFIX + "§cDieser Spieler konnte nicht gefunden werden!");
					}
				}
			} else {
				p.sendMessage(PREFIX + LanguageTools.getNO_PERMISSION());
			}
		} else {
			Bukkit.getConsoleSender().sendMessage(PREFIX + LanguageTools.getONLY_PLAYER());
		}
		return false;
	}
}
