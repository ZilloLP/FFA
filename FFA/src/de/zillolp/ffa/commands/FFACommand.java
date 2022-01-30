package de.zillolp.ffa.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.LanguageTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.config.tools.PermissionTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.profiles.InventoryProfil;
import de.zillolp.ffa.profiles.PlayerProfil;
import de.zillolp.ffa.utils.ConfigUtil;

public class FFACommand implements CommandExecutor {
	private HashMap<Player, PlayerProfil> playerprofiles = Main.getInstance().playerprofiles;
	private HashMap<Player, InventoryProfil> invprofiles = Main.getInstance().invprofiles;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		final String PREFIX = LanguageTools.getPREFIX();
		final boolean english = ConfigTools.getEnglish();
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				sendInfo(player);
			} else if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
				player.sendMessage("§6§lInfos zum Plugin:");
				player.sendMessage("§7Plugin Name: §eFFA");
				player.sendMessage("§7Plugin Version: §e" + Main.getInstance().getDescription().getVersion());
				player.sendMessage("§7Author: §eZilloLP");
				player.sendMessage("§7Discord: §ehttps://discord.gg/NBs27JK");
			} else if (player.hasPermission(PermissionTools.getADMIN_PERMISSION())) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("reload")) {
						Main.getInstance().reload();
						if (english) {
							player.sendMessage(PREFIX + "§7The plugin was reloaded §asuccesful§7!");
						} else {
							player.sendMessage(PREFIX + "§7Plugin wurde §aerfolgreich §7neu geladen!");
						}
					} else if (args[0].equalsIgnoreCase("arenas")) {
						ConfigUtil configutil = Main.getInstance().getConfigCreation().getManager().getNewConfig("locations.yml");
						ConfigurationSection configsection = configutil.getConfigurationSection("Arenas");
						if (configsection != null && configsection.getKeys(false).size() > 0) {
							if (english) {
								player.sendMessage("§6All arenas:");
							} else {
								player.sendMessage("§6Alle Arenen:");
							}
							for (String arena : configsection.getKeys(false)) {
								if (Main.getInstance().getArenaManager().names.contains(arena)) {
									player.sendMessage("§7- §e" + arena + " §7» §a✔");
								} else {
									player.sendMessage("§7- §e" + arena + " §7» §c✖");
								}
							}
						} else {
							if (english) {
								player.sendMessage(PREFIX + "§cNo arena has been created yet.");
							} else {
								player.sendMessage(PREFIX + "§cEs wurde noch keine Arena erstellt.");
							}
						}
					} else {
						sendInfo(player);
					}
				} else if (args.length == 2) {
					String name = args[1];
					LocationTools locationtools = new LocationTools(name);
					if (args[0].equalsIgnoreCase("create")) {
						if (!(locationtools.isArena())) {
							locationtools.saveArena();
							if (english) {
								player.sendMessage(PREFIX + "§7You created the Arena §b" + name + "§7.");
							} else {
								player.sendMessage(PREFIX + "§7Du hast die Arena §b" + name + " §7erstellt.");
							}
						} else {
							if (english) {
								player.sendMessage(PREFIX + "§cThis arena already exists!");
							} else {
								player.sendMessage(PREFIX + "§cDiese Arena existiert bereits!");
							}
						}
					} else if (args[0].equalsIgnoreCase("edit")) {
						if (locationtools.isArena()) {
							PlayerProfil playerprofil = playerprofiles.get(player);
							playerprofil.setArena(name);
							InventoryProfil invprofil = invprofiles.get(player);
							Inventory inv = invprofil.getArenainv();
							if (inv == null) {
								inv = invprofil.setArenainv(Bukkit.createInventory(null, 3 * 9, Main.getInstance().getInventorySetter().TITLE));
							}
							Main.getInstance().getInventorySetter().setDesign(player, inv);
							Main.getInstance().getInventorySetter().setArenainv(player, inv);
							player.openInventory(inv);
						} else {
							if (english) {
								player.sendMessage(PREFIX + "§cThis arena doesn't exist!");
							} else {
								player.sendMessage(PREFIX + "§cDiese Arena existiert nicht!");
							}
						}
					} else if (args[0].equalsIgnoreCase("delete")) {
						if (locationtools.isArena()) {
							locationtools.resetArena();
							Main.getInstance().getArenaManager().loadArenas();
							if (english) {
								player.sendMessage(PREFIX + "§7You deleted the arena §b" + name + "§7.");
							} else {
								player.sendMessage(PREFIX + "§7Du hast die Arena §b" + name + " §7gelöscht.");
							}
						} else {
							if (english) {
								player.sendMessage(PREFIX + "§cThis arena doesn't exist!");
							} else {
								player.sendMessage(PREFIX + "§cDiese Arena existiert nicht!");
							}
						}
					} else {
						sendInfo(player);
					}
				} else if (args.length == 3) {
					String name = args[1];
					LocationTools locationtools = new LocationTools(name);
					if (args[0].equalsIgnoreCase("rename")) {
						if (locationtools.isArena()) {
							String rename = args[2];
							if (!(new LocationTools(rename).isArena())) {
								locationtools.renameArena(rename);
								Main.getInstance().getArenaManager().loadArenas();
								if (english) {
									player.sendMessage(
											PREFIX + "§7The arena §b" + name + " §7is now called §9" + rename + ".");
								} else {
									player.sendMessage(PREFIX + "§7Die Arena §b" + name + " §7heißt nun §9" + rename + ".");
								}
							} else {
								if (english) {
									player.sendMessage(PREFIX + "§cThis arena already exists!");
								} else {
									player.sendMessage(PREFIX + "§cDiese Arena existiert bereits!");
								}
							}
						} else {
							if (english) {
								player.sendMessage(PREFIX + "§cThis arena doesn't exist!");
							} else {
								player.sendMessage(PREFIX + "§cDiese Arena existiert nicht!");
							}
						}
					}
				} else {
					sendInfo(player);
				}
			} else {
				player.sendMessage(PREFIX + LanguageTools.getNO_PERMISSION());
			}
		} else if (args[0].equalsIgnoreCase("reload")) {
			Main.getInstance().reload();
			if (english) {
				Bukkit.getConsoleSender().sendMessage(PREFIX + "§7The plugin was reloaded §asuccesful§7!");
			} else {
				Bukkit.getConsoleSender().sendMessage(PREFIX + "§7Das Plugin wurde §aerfolgreich §7neu geladen.");
			}
		} else {
			Bukkit.getConsoleSender().sendMessage(PREFIX + LanguageTools.getONLY_PLAYER());
		}
		return false;
	}

	private void sendInfo(Player p) {
		boolean english = ConfigTools.getEnglish();
		if (english) {
			p.sendMessage("§c§lFFA Commands:");
			p.sendMessage("§e/ffa info.");
			p.sendMessage("§7Shows infos about the plugin.");
			if (p.hasPermission(PermissionTools.getADMIN_PERMISSION())) {
				p.sendMessage("§e/ffa reload");
				p.sendMessage("§7This reloads all settings.");
				p.sendMessage("§e/ffa arenas");
				p.sendMessage("§7Shows you a list of all arenas.");
				p.sendMessage("§e/ffa create <Name>");
				p.sendMessage("§7Create an FFA arena.");
				p.sendMessage("§e/ffa edit <Name>");
				p.sendMessage("§7Set an arena.");
				p.sendMessage("§e/ffa rename <Name> <Rename>");
				p.sendMessage("§7Rename an arena.");
				p.sendMessage("§e/ffa delete <Name>");
				p.sendMessage("§7Deletes an FFA arena.");
			}
		} else {
			p.sendMessage("§c§lFFA Befehle:");
			p.sendMessage("§e/ffa info.");
			p.sendMessage("§7Zeigt dir die Infos zum Plugin.");
			if (p.hasPermission(PermissionTools.getADMIN_PERMISSION())) {
				p.sendMessage("§e/ffa reload");
				p.sendMessage("§7Lädt das alle Einstellungen neu.");
				p.sendMessage("§e/ffa arenas");
				p.sendMessage("§7Zeigt dir eine Liste aller Arenen.");
				p.sendMessage("§e/ffa create <Name>");
				p.sendMessage("§7Erstellt eine FFA Arena.");
				p.sendMessage("§e/ffa edit <Name>");
				p.sendMessage("§7Stellt eine Arena ein.");
				p.sendMessage("§e/ffa rename <Name> <Rename>");
				p.sendMessage("§7Benennt eine Arena neu.");
				p.sendMessage("§e/ffa delete <Name>");
				p.sendMessage("§7Löscht eine FFA Arena.");
			}
		}
	}
}
