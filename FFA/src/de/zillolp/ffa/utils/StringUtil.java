package de.zillolp.ffa.utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Scanner;

import org.bukkit.entity.Player;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.map.ArenaChanger;
import de.zillolp.ffa.profiles.PlayerProfil;

public class StringUtil {

	public static boolean isNumeric(String value, int max) {
		try {
			int number = Integer.parseInt(value);
			if (number <= 0)
				return false;
			return number <= max;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static String replaceDefaults(String message) {
		if (message.contains("%Ae%")) {
			message = message.replace("%Ae%", "Ä");
		}
		if (message.contains("%ae%")) {
			message = message.replace("%ae%", "ä");
		}
		if (message.contains("%Oe%")) {
			message = message.replace("%Oe%", "Ö");
		}
		if (message.contains("%oe%")) {
			message = message.replace("%oe%", "ö");
		}
		if (message.contains("%Ue%")) {
			message = message.replace("%Ue%", "Ü");
		}
		if (message.contains("%ue%")) {
			message = message.replace("%ue%", "ü");
		}
		if (message.contains("%sz%")) {
			message = message.replace("%sz%", "ß");
		}
		if (message.contains("%>%")) {
			message = message.replace("%>%", "»");
		}
		if (message.contains("%<%")) {
			message = message.replace("%<%", "«");
		}
		if (message.contains("%*%")) {
			message = message.replace("%*%", "×");
		}
		if (message.contains("%->%")) {
			message = message.replace("%->%", "➜");
		}
		if (message.contains("%<3%")) {
			message = message.replace("%<3%", "❤");
		}
		if (message.contains("&")) {
			message = message.replace("&", "§");
		}
		return message;
	}

	public static String replaceScoreboard(Player p, String message, String empty) {
		message = replaceDefaults(message);
		if (message.contains("%blank%")) {
			message = message.replace("%blank%", "" + empty);
		}
		message = replaceKills(p, message);
		message = replaceMap(message);
		message = replaceDeaths(p, message);
		message = replaceStreak(p, message);
		message = replaceTime(message);
		return message;
	}

	public static String replaceActionbar(Player p, String message) {
		message = replaceDefaults(message);
		message = replaceKills(p, message);
		message = replaceMap(message);
		message = replaceTeams(message);
		message = replaceDeaths(p, message);
		message = replaceStreak(p, message);
		message = replaceTime(message);
		return message;
	}

	public static String replaceName(Player p, String message) {
		if (message.contains("%name%")) {
			message = message.replace("%name%", p.getName());
		}
		return message;
	}

	public static String replaceHearts(Player p, String message) {
		if (message.contains("%hearts%")) {
			NumberFormat n = NumberFormat.getInstance();
			n.setMaximumFractionDigits(2);
			message = message.replace("%hearts%", n.format(p.getHealth() / 2));
		}
		return message;
	}

	public static String replaceKills(Player p, String message) {
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (message.contains("%kills%")) {
				message = message.replace("%kills%", String.valueOf(playerprofil.getKills()));
			}
		}
		return message;
	}

	public static String replaceMap(String message) {
		if (message.contains("%map%")) {
			String arena = Main.getInstance().getArenaManager().activeArena;
			if (arena != null) {
				message = message.replace("%map%", Main.getInstance().getArenaManager().activeArena);
			} else {
				message = message.replace("%map%", "?");
			}
		}
		return message;
	}

	public static String replaceTeams(String message) {
		if (message.contains("%teams%")) {
			String arena = Main.getInstance().getArenaManager().activeArena;
			if (arena != null) {
				LocationTools locationtools = new LocationTools(arena);
				if (locationtools.getTeams()) {
					if (ConfigTools.getEnglish()) {
						message = message.replace("%teams%", "§aAllowed");
					} else {
						message = message.replace("%teams%", "§aErlaubt");
					}
				} else {
					if (ConfigTools.getEnglish()) {
						message = message.replace("%teams%", "§cNot allowed");
					} else {
						message = message.replace("%teams%", "§cVerboten");
					}
				}
			} else {
				message = message.replace("%teams%", "§c?");
			}
		}
		return message;
	}

	public static String replaceDeaths(Player p, String message) {
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (message.contains("%deaths%")) {
				message = message.replace("%deaths%", String.valueOf(playerprofil.getDeaths()));
			}
		}
		return message;
	}

	public static String replaceStreak(Player p, String message) {
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (message.contains("%streak%")) {
				message = message.replace("%streak%", String.valueOf(playerprofil.getKillstreak()));
			}
		}
		return message;
	}

	public static String replaceSeconds(String message) {
		if (message.contains("%seconds%")) {
			message = message.replace("%seconds%", String.valueOf(ArenaChanger.seconds));
		}
		return message;
	}

	public static String replaceTime(String message) {
		if (message.contains("%time%")) {
			ArenaChanger arenachanger = Main.getInstance().getArenaManager().arenaChanger;
			if (arenachanger != null) {
				int seconds = ArenaChanger.seconds;
				int Hours = (seconds % 86400) / 3600;
				int Minutes = ((seconds % 86400) % 3600) / 60;
				int Seconds = ((seconds % 86400) % 3600) % 60;
				message = message.replace("%time%", getFormat(Hours, Minutes, Seconds));
			} else {
				if (ConfigTools.getMapchange()) {
					message = message.replace("%time%", "§c??:??:??");
				} else {
					if (ConfigTools.getEnglish()) {
						message = message.replace("%time%", "§cMapchange has been switched off!");
					} else {
						message = message.replace("%time%", "§cMapchange wurde ausgeschaltet!");	
					}
				}
			}
		}
		return message;
	}

	private static String getFormat(int hours, int minutes, int seconds) {
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public static String getUUIDByName(String name) {
		String uuid = null;

		String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
		String result;
		try {
			result = readString(url);
			JsonElement root = new JsonParser().parse(result);
			uuid = root.getAsJsonObject().get("id").getAsString();

		} catch (Exception e2) {

		}

		return uuid;

	}

	public static String readString(String requestURL) {
		try {
			final URLConnection connection = new URL(requestURL).openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla/4.0");

			try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.toString())) {
				scanner.useDelimiter("\\A");
				return scanner.hasNext() ? scanner.next() : "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return requestURL;
	}
}
