package de.zillolp.ffa.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.KitTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.profiles.PlayerProfil;
import de.zillolp.ffa.xclasses.XMaterial;

public class InventorySetter extends ItemCreator {
	private static HashMap<Player, PlayerProfil> playerprofiles = Main.getInstance().playerprofiles;
	private static String texture = "http://textures.minecraft.net/texture/";
	public static String TITLE;
	public static String SET_KIT;
	public static String TEAMS_ALLOWED;
	public static String TEAMS_NOT_ALLOWED;
	public static String BLOCKS_ACTIVATED;
	public static String BLOCKS_DEACTIVATED;
	public static String SET_SIGN;
	public static String SET_UPPERCORNER;
	public static String SET_SPAWN;
	public static String SET_BUTTOMCORNER;

	public InventorySetter() {
		boolean english = ConfigTools.getEnglish();
		if (english) {
			TITLE = "§8Options";
			SET_KIT = "§7Set kit";
			TEAMS_ALLOWED = "§7Teams§8: §aAllowed";
			TEAMS_NOT_ALLOWED = "§7Teams§8: §cNot allowed";
			BLOCKS_ACTIVATED = "§7Place Blocks §8: §aActivated";
			BLOCKS_DEACTIVATED = "§7Place Blocks §8: §cDeactivated";
			SET_SIGN = "Set join sign";
			SET_UPPERCORNER = "§7Set Upper Corner";
			SET_SPAWN = "§7Set Spawn";
			SET_BUTTOMCORNER = "§7Set Buttom Corner";
		} else {
			TITLE = "§8Einstellungen";
			SET_KIT = "§7Kit setzen";
			TEAMS_ALLOWED = "§7Teams§8: §aErlaubt";
			TEAMS_NOT_ALLOWED = "§7Teams§8: §cVerboten";
			BLOCKS_ACTIVATED = "§7Blöcke Platzieren§8: §aAktiviert";
			BLOCKS_DEACTIVATED = "§7Blöcke Platzieren§8: §cDeaktiviert";
			SET_SIGN = "§7Join Schild setzen";
			SET_UPPERCORNER = "§7Obere Ecke setzen";
			SET_SPAWN = "§7Spawn setzen";
			SET_BUTTOMCORNER = "§7Untere Ecke setzen";
		}
	}

	public static void setDesign(Player p, Inventory inv) {
		ItemStack glass = createItem(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem(), "§7*");
		IntStream.range(0, inv.getSize()).forEach(i -> inv.setItem(i, glass));
	}

	public static void setArenainv(Player p, Inventory inv) {
		boolean english = ConfigTools.getEnglish();
		PlayerProfil playerprofil = playerprofiles.get(p);
		String arena = playerprofil.getArena();
		LocationTools locationtools = new LocationTools(playerprofil.getArena());

		String SET;
		String NOT_SET;
		if (english) {
			SET = "§7» §aSet";
			NOT_SET = "§7» §cNot set";
		} else {
			SET = "§7» §aGesetzt";
			NOT_SET = "§7» §cNicht gesetzt";
		}
		ArrayList<String> lore = new ArrayList<>();
		ItemStack kit;
		lore.add("§n");
		if (english) {
			lore.add("§eLeft click §8- §7Set kit.");
			lore.add("§eRight click §8- §7Load kit.");
		} else {
			lore.add("§eLinksklick §8- §7Kit setzen.");
			lore.add("§eRechtsklick §8- §7Kit laden.");
		}
		lore.add("§7");
		if (KitTools.isKit(arena)) {
			lore.add(SET);
			kit = createItem(XMaterial.IRON_SWORD.parseItem(), SET_KIT, lore, true, ItemFlag.HIDE_ATTRIBUTES);
		} else {
			lore.add(NOT_SET);
			kit = createItem(XMaterial.IRON_SWORD.parseItem(), SET_KIT, lore, ItemFlag.HIDE_ATTRIBUTES);
		}
		lore.clear();

		ItemStack teams;
		if (locationtools.getTeams()) {
			if (english) {
				lore.add("§7» Players are allowed to team");
			} else {
				lore.add("§7» Spieler dürfen Teamen");
			}
			teams = createItem(XMaterial.BEACON.parseItem(), TEAMS_ALLOWED, lore, true);
		} else {
			if (english) {
				lore.add("§7» Players arent allowed to team");
			} else {
				lore.add("§7» Spieler dürfen nicht Teamen");
			}
			teams = createItem(XMaterial.BEACON.parseItem(), TEAMS_NOT_ALLOWED, lore);
		}
		lore.clear();

		ItemStack blocks;
		if (locationtools.getBuild()) {
			if (english) {
				lore.add("§7» Players can build");
			} else {
				lore.add("§7» Spieler können Bauen");
			}
			blocks = createItem(XMaterial.SANDSTONE.parseItem(), BLOCKS_ACTIVATED, lore, true);
		} else {
			if (english) {
				lore.add("§7» Players cannot build");
			} else {
				lore.add("§7» Spieler können nicht Bauen");
			}
			blocks = createItem(XMaterial.SANDSTONE.parseItem(), BLOCKS_DEACTIVATED, lore);
		}
		lore.clear();
		inv.setItem(10, kit);
		inv.setItem(11, teams);
		inv.setItem(12, blocks);

		if (!(ConfigTools.getBungeecord())) {
			ItemStack sign;
			if (locationtools.isLocation("Sign")) {
				lore.add(SET);
				sign = createItem(XMaterial.OAK_SIGN.parseItem(), SET_SIGN, lore, true);
			} else {
				lore.add(NOT_SET);
				sign = createItem(XMaterial.OAK_SIGN.parseItem(), SET_SIGN, lore);
			}
			lore.clear();
			inv.setItem(13, sign);
		}

		ItemStack left;
		if (locationtools.isLocation("Uppercorner")) {
			lore.add(SET);
			left = getSkullByTextureURL(texture + "865426a33df58b465f0601dd8b9bec3690b2193d1f9503c2caab78f6c2438",
					SET_UPPERCORNER, lore);
		} else {
			lore.add(NOT_SET);
			left = getSkullByTextureURL(texture + "865426a33df58b465f0601dd8b9bec3690b2193d1f9503c2caab78f6c2438",
					SET_UPPERCORNER, lore);
		}
		lore.clear();

		ItemStack spawn;
		if (locationtools.isLocation("Spawn")) {
			lore.add(SET);
			spawn = createItem(XMaterial.MAGMA_CREAM.parseItem(), SET_SPAWN, lore, true);
		} else {
			lore.add(NOT_SET);
			spawn = createItem(XMaterial.MAGMA_CREAM.parseItem(), SET_SPAWN, lore);
		}
		lore.clear();

		ItemStack right;
		if (locationtools.isLocation("Bottomcorner")) {
			lore.add(SET);
			right = getSkullByTextureURL(texture + "35cbdb28991a16eb2c793474ef7d0f458a5d13fffc283c4d74d929941bb1989",
					SET_BUTTOMCORNER, lore);
		} else {
			lore.add(NOT_SET);
			right = getSkullByTextureURL(texture + "35cbdb28991a16eb2c793474ef7d0f458a5d13fffc283c4d74d929941bb1989",
					SET_BUTTOMCORNER, lore);
		}
		lore.clear();

		inv.setItem(14, left);
		inv.setItem(15, spawn);
		inv.setItem(16, right);
	}

}
