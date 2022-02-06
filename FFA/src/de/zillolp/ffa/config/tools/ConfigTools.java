package de.zillolp.ffa.config.tools;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Material;

import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.utils.ConfigUtil;
import de.zillolp.ffa.xclasses.XMaterial;

public class ConfigTools {
	private ConfigUtil configutil;
	private static boolean Bungeecord;
	private static boolean MySQL;
	private static boolean English;
	private static boolean Scoreboard;
	private static boolean Actionbar;
	private static boolean Unbreakable;
	private static GameMode Gamemode;
	private static boolean Mapchange;
	private static int MapchangeTime;
	private static boolean Falldamage;
	private static int BuildingProtection;
	private static XMaterial ReplaceType;
	private static int ReplaceTime;
	private static int AirTime;
	private static boolean Infiniteblocks;
	private static ArrayList<Material> blocked_blocks;

	public ConfigTools() {
		this.configutil = Main.getInstance().getConfigCreation().getManager().getNewConfig("config.yml");
//		Bungeecord = configutil.getBoolean("Bungeecord");
		Bungeecord = true;
		MySQL = configutil.getBoolean("MySQL");
		English = configutil.getBoolean("English");
		Scoreboard = configutil.getBoolean("Scoreboard");
		Actionbar = configutil.getBoolean("Actionbar");
		Unbreakable = configutil.getBoolean("Unbreakable");
		Gamemode = GameMode.valueOf(configutil.getString("Gamemode"));
		Mapchange = configutil.getBoolean("Mapchange");
		MapchangeTime = configutil.getInt("Mapchange Time") * 60;
		Falldamage = configutil.getBoolean("Falldamage");
		BuildingProtection = configutil.getInt("Building Protection");
		ReplaceType = XMaterial.valueOf(configutil.getString("Replace Type"));
		ReplaceTime = configutil.getInt("Replace Time") * 20;
		AirTime = configutil.getInt("Air Time") * 20;
		Infiniteblocks = configutil.getBoolean("Infinite blocks");
		blocked_blocks = new ArrayList<>();
		String section = "Protected Blocks";
		for (String block : configutil.getConfigurationSection(section).getKeys(false)) {
			Material blocked_block = XMaterial.valueOf(configutil.getString(section + "." + block).toUpperCase())
					.parseMaterial();
			if (!(blocked_blocks.contains(blocked_block))) {
				blocked_blocks.add(blocked_block);
			}
		}
	}

	public static boolean getBungeecord() {
		return Bungeecord;
	}

	public static boolean getMySQL() {
		return MySQL;
	}

	public static boolean getEnglish() {
		return English;
	}

	public static boolean getScoreboard() {
		return Scoreboard;
	}

	public static boolean getActionbar() {
		return Actionbar;
	}

	public static boolean getUnbreakable() {
		return Unbreakable;
	}

	public static GameMode getGamemode() {
		return Gamemode;
	}

	public static boolean getMapchange() {
		return Mapchange;
	}

	public static int getMapchangeTime() {
		return MapchangeTime;
	}

	public static boolean getFalldamage() {
		return Falldamage;
	}

	public static int getBuildingProtection() {
		return BuildingProtection;
	}

	public static XMaterial getReplaceType() {
		return ReplaceType;
	}

	public static int getReplaceTime() {
		return ReplaceTime;
	}

	public static int getAirTime() {
		return AirTime;
	}

	public static boolean getInfiniteblocks() {
		return Infiniteblocks;
	}

	public static void setMapchange(boolean mapchange) {
		Mapchange = mapchange;
	}

	public static ArrayList<Material> getBlocked_blocks() {
		return blocked_blocks;
	}
}
