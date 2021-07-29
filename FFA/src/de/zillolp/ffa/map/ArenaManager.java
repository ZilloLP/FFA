package de.zillolp.ffa.map;

import java.io.File;
import java.util.LinkedList;
import java.util.Random;

import org.bukkit.configuration.ConfigurationSection;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.KitTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.utils.ConfigUtil;

public class ArenaManager {
	public static String active_arena;
	public static ArenaChanger arenachanger;
	public static LinkedList<String> names;

	public ArenaManager() {
		loadArenas();
		if (active_arena != null) {
			if (arenachanger != null) {
				arenachanger.stop();
				arenachanger = null;
			}
			if (ConfigTools.getMapchange()) {
				arenachanger = new ArenaChanger();
			}
		}
	}

	public static void loadArenas() {
		checkArenas();
		if (names.size() > 1) {
			if (ConfigTools.getMapchange()) {
				if (active_arena != null) {
					String arena = names.get(new Random().nextInt(names.size()));
					if (active_arena.equalsIgnoreCase(arena)) {
						names.remove(active_arena);
						arena = names.get(new Random().nextInt(names.size()));
						names.add(active_arena);
					}
					active_arena = arena;
				} else {
					active_arena = names.get(new Random().nextInt(names.size()));
				}
			} else {
				active_arena = names.get(0);
			}
		} else if (names.size() == 1) {
			ConfigTools.setMapchange(false);
			active_arena = names.get(0);
		}
	}

	public static void checkArenas() {
		ConfigUtil configutil = new ConfigUtil(new File("plugins/FFA/locations.yml"));
		ConfigurationSection section = configutil.getConfigsection("Arenas");
		names = new LinkedList<>();
		if (section != null) {
			if (section.getKeys(false).size() > 0) {
				for (String arena : section.getKeys(false)) {
					LocationTools locationtools = new LocationTools(arena);
					if (locationtools.isPlayable() && KitTools.isKit(arena)) {
						names.add(arena);
					}
				}
			}
		}
	}

	public static void refresh() {
		loadArenas();
		if (active_arena != null) {
			if (arenachanger != null) {
				arenachanger.stop();
				arenachanger = null;
			}
			if (ConfigTools.getMapchange()) {
				arenachanger = new ArenaChanger();
			}
		}
	}
}
