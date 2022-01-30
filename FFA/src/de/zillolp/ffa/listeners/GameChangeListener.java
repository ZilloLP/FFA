package de.zillolp.ffa.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.zillolp.ffa.config.tools.KitTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.map.ArenaManager;
import de.zillolp.ffa.profiles.PlayerProfil;

public class GameChangeListener implements Listener {

	@EventHandler
	public void on(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (!(playerprofil.getBuildmode()) && playerprofil.getJoined()) {
				String arena = ArenaManager.active_arena;
				if (arena != null) {
					LocationTools locationtools = new LocationTools(arena);
					Location loc = p.getLocation();

					if (locationtools != null) {
						if (checkOnSpawn(loc, locationtools, 0)) {
							if (playerprofil.getIngame()) {
								p.getInventory().setArmorContents(null);
								p.getInventory().clear();
								playerprofil.setIngame(false);
							}
						} else {
							if (!(playerprofil.getIngame())) {
								KitTools kittools = new KitTools(arena);
								kittools.loadKit(p);
								playerprofil.setIngame(true);
							}
						}
					}
				}
			}
		}
	}

	public static boolean checkOnSpawn(Location loc, LocationTools locationtools, int range) {
		double x = loc.getBlockX();
		double y = loc.getBlockY();
		double z = loc.getBlockZ();

		Location bottom_loc = locationtools.loadLocation("Bottomcorner");
		double bottom_x = bottom_loc.getBlockX();
		double bottom_y = bottom_loc.getBlockY();
		double bottom_z = bottom_loc.getBlockZ();

		Location upper_loc = locationtools.loadLocation("Uppercorner");
		double upper_x = upper_loc.getBlockX();
		double upper_y = upper_loc.getBlockY();
		double upper_z = upper_loc.getBlockZ();

		double min_x = 0;
		double max_x = 0;
		if (bottom_x >= upper_x) {
			max_x = bottom_x;
			min_x = upper_x;
		} else if (upper_x > bottom_x) {
			max_x = upper_x;
			min_x = bottom_x;
		}

		double min_z = 0;
		double max_z = 0;
		if (bottom_z >= upper_z) {
			max_z = bottom_z;
			min_z = upper_z;
		} else if (upper_z > bottom_z) {
			max_z = upper_z;
			min_z = bottom_z;
		}

		min_x = min_x - range;
		max_x = max_x + range;
		bottom_y = bottom_y - range;
		upper_y = upper_y + range;
		min_z = min_z - range;
		max_z = max_z + range;

		return min_x <= x && max_x >= x && min_z <= z && max_z >= z && y >= bottom_y && y <= upper_y;
	}
}
