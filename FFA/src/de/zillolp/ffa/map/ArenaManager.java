package de.zillolp.ffa.map;

import java.util.LinkedList;
import java.util.Random;

import org.bukkit.configuration.ConfigurationSection;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.KitTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.utils.ConfigUtil;

public class ArenaManager {
	public String active_arena;
	public ArenaChanger arenachanger;
	public LinkedList<String> names;

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

	public void loadArenas() {
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

	public void checkArenas() {
		ConfigUtil configutil = Main.getInstance().getConfigCreation().getManager().getNewConfig("locations.yml");
		ConfigurationSection section = configutil.getConfigurationSection("Arenas");
		names = new LinkedList<>();
		if (section != null) {
			if (section.getKeys(false).size() > 0) {
				for (String arena : section.getKeys(false)) {
					LocationTools locationtools = new LocationTools(arena);
					if (locationtools.isPlayable() && KitTools.hasKit(arena)) {
						names.add(arena);
					}
				}
			}
		}
	}

	public void refresh() {
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
