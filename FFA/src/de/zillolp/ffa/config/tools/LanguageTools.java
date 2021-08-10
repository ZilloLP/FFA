package de.zillolp.ffa.config.tools;

import org.bukkit.entity.Player;

import de.zillolp.ffa.config.ConfigCreation;
import de.zillolp.ffa.utils.ConfigUtil;
import de.zillolp.ffa.utils.StringUtil;

public class LanguageTools extends StringUtil {
	private ConfigUtil configutil;
	private static String PREFIX;
	private static String NO_PERMISSION;
	private static String ONLY_PLAYER;
	private static String JOIN_MESSAGE;
	private static String QUIT_MESSAGE;
	private static String BUILD_PROTECTION;
	private static String PLAYER_DIED;
	private static String KILLED_PLAYER;
	private static String DIED_BY_PLAYER;
	private static String KILLSTREAK_WIN;
	private static String KILLSTREAK_LOSE;
	private static String ARENA_CHANGE_SECONDS;
	private static String ARENA_CHANGE_SECOND;
	private static String ARENA_CHANGED;

	public LanguageTools() {
		configutil = ConfigCreation.manager.getNewConfig("language.yml");
		PREFIX = replaceDefaults(configutil.getString("PREFIX"));
		NO_PERMISSION = replaceDefaults(configutil.getString("NO_PERMISSION"));
		ONLY_PLAYER = replaceDefaults(configutil.getString("ONLY_PLAYER"));
		JOIN_MESSAGE = replaceDefaults(configutil.getString("JOIN_MESSAGE"));
		QUIT_MESSAGE = replaceDefaults(configutil.getString("QUIT_MESSAGE"));
		BUILD_PROTECTION = replaceDefaults(configutil.getString("BUILD_PROTECTION"));
		PLAYER_DIED = replaceDefaults(configutil.getString("PLAYER_DIED"));
		KILLED_PLAYER = replaceDefaults(configutil.getString("KILLED_PLAYER"));
		DIED_BY_PLAYER = replaceDefaults(configutil.getString("DIED_BY_PLAYER"));
		KILLSTREAK_WIN = replaceDefaults(configutil.getString("KILLSTREAK_WIN"));
		KILLSTREAK_LOSE = replaceDefaults(configutil.getString("KILLSTREAK_LOSE"));
		ARENA_CHANGE_SECONDS = replaceDefaults(configutil.getString("ARENA_CHANGE_SECONDS"));
		ARENA_CHANGE_SECOND = replaceDefaults(configutil.getString("ARENA_CHANGE_SECOND"));
		ARENA_CHANGED = replaceDefaults(configutil.getString("ARENA_CHANGED"));
	}

	public static String getPREFIX() {
		return PREFIX;
	}

	public static String getNO_PERMISSION() {
		return NO_PERMISSION;
	}

	public static String getONLY_PLAYER() {
		return ONLY_PLAYER;
	}

	public static String getJOIN_MESSAGE(Player p) {
		return replaceName(p, JOIN_MESSAGE);
	}

	public static String getQUIT_MESSAGE(Player p) {
		return replaceName(p, QUIT_MESSAGE);
	}

	public static String getBUILD_PROTECTION() {
		return BUILD_PROTECTION;
	}

	public static String getPLAYER_DIED() {
		return PLAYER_DIED;
	}

	public static String getKILLED_PLAYER(Player p, Player k) {
		return replaceHearts(k, replaceName(p, KILLED_PLAYER));
	}

	public static String getDIED_BY_PLAYER(Player p, Player k) {
		return replaceHearts(p, replaceName(k, DIED_BY_PLAYER));
	}

	public static String getKILLSTREAK_WIN(Player p) {
		return replaceStreak(p, replaceName(p, KILLSTREAK_WIN));
	}

	public static String getKILLSTREAK_LOSE(Player p) {
		return replaceStreak(p, replaceName(p, KILLSTREAK_LOSE));
	}

	public static String getARENA_CHANGE_SECONDS() {
		return replaceSeconds(ARENA_CHANGE_SECONDS);
	}

	public static String getARENA_CHANGE_SECOND() {
		return replaceSeconds(ARENA_CHANGE_SECOND);
	}

	public static String getARENA_CHANGED() {
		return replaceMap(ARENA_CHANGED);
	}
}
