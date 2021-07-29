package de.zillolp.ffa.config.tools;

import java.io.File;

import org.bukkit.GameMode;

import de.zillolp.ffa.utils.ConfigUtil;
import de.zillolp.ffa.xclasses.XMaterial;

public class ConfigTools {
	private ConfigUtil configutil;
	private static boolean MySQL;
	private static boolean English;
	private static boolean Scoreboard;
	private static boolean Actionbar;
	private static boolean Unbreakable;
	private static GameMode Gamemode;
	private static boolean Mapchange;
	private static int MapchangeTime;
	private static int BuildingProtection;
	private static XMaterial ReplaceType;
	private static int ReplaceTime;
	private static int AirTime;

	public ConfigTools() {
		this.configutil = new ConfigUtil(new File("plugins/FFA/config.yml"));
		MySQL = configutil.getBoolean("MySQL");
		English = configutil.getBoolean("English");
		Scoreboard = configutil.getBoolean("Scoreboard");
		Actionbar = configutil.getBoolean("Actionbar");
		Unbreakable = configutil.getBoolean("Unbreakable");
		Gamemode = GameMode.valueOf(configutil.getString("Gamemode"));
		Mapchange = configutil.getBoolean("Mapchange");
		MapchangeTime = configutil.getInt("Mapchange Time") * 60;
		BuildingProtection = configutil.getInt("Building Protection");
		ReplaceType =  XMaterial.valueOf(configutil.getString("Replace Type"));
		ReplaceTime = configutil.getInt("Replace Time") * 20;
		AirTime = configutil.getInt("Air Time") * 20;
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
	
	public static void setMapchange(boolean mapchange) {
		Mapchange = mapchange;
	}
}
