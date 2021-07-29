package de.zillolp.ffa.map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.KitTools;
import de.zillolp.ffa.config.tools.LanguageTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.listeners.BlockPlaceListener;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.profiles.PlayerProfil;

public class ArenaChanger implements Runnable {
	private static int sched;
	public static int seconds;

	public ArenaChanger() {
		seconds = ConfigTools.getMapchangeTime() + 1;
		sched = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, this, 0, 20);
	}

	@Override
	public void run() {
		String PREFIX = LanguageTools.getPREFIX();
		seconds--;
		switch (seconds) {
		case 60:
		case 30:
		case 15:
		case 10:
		case 5:
		case 4:
		case 3:
		case 2:
			Bukkit.broadcastMessage(PREFIX + LanguageTools.getARENA_CHANGE_SECONDS());
			break;
		case 1:
			Bukkit.broadcastMessage(PREFIX + LanguageTools.getARENA_CHANGE_SECOND());
			break;
		case 0:
			ArenaManager.loadArenas();
			String arena = ArenaManager.active_arena;
			LocationTools locationtools =  new LocationTools(arena);
			Location loc = locationtools.loadLocation("Spawn");
			BlockPlaceListener.replaceAll();
			for (Player all : Bukkit.getOnlinePlayers()) {
				PlayerProfil playerprofil = Main.playerprofiles.get(all);
				playerprofil.setKittools(new KitTools(arena));
				all.getInventory().setArmorContents(null);
				all.getInventory().clear();
				all.setLevel(0);
				all.setExp(0);
				all.setFoodLevel(20);
				all.setHealth(20);
				all.setGameMode(ConfigTools.getGamemode());
				all.teleport(loc);
			}
			Bukkit.broadcastMessage(PREFIX + LanguageTools.getARENA_CHANGED());
			seconds = ConfigTools.getMapchangeTime() + 1;
			break;
		}
	}

	public void stop() {
		Bukkit.getScheduler().cancelTask(sched);
	}
}
