package de.zillolp.ffa.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.LanguageTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.map.ArenaManager;
import de.zillolp.ffa.profiles.PlayerProfil;
import de.zillolp.ffa.stats.DatenManager;

public class PlayerConnectionListener implements Listener {
	private HashMap<Player, PlayerProfil> playerprofiles = Main.playerprofiles;

	@EventHandler
	public void on(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		DatenManager.createPlayer(p);
		Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {

			@Override
			public void run() {
				PlayerProfil profil = new PlayerProfil(p);
				if (playerprofiles.containsKey(p)) {
					playerprofiles.replace(p, profil);
				} else {
					playerprofiles.put(p, profil);
				}
			}
		}, 5);
		e.setJoinMessage(LanguageTools.getJOIN_MESSAGE(p));

		String arena = ArenaManager.active_arena;
		if (arena != null) {
			LocationTools locationtools = new LocationTools(arena);
			p.getInventory().setArmorContents(null);
			p.getInventory().clear();
			p.setLevel(0);
			p.setExp(0);
			p.setFoodLevel(20);
			p.setHealth(20);
			p.setGameMode(ConfigTools.getGamemode());
			p.teleport(locationtools.loadLocation("Spawn"));
		}
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (playerprofiles.containsKey(p)) {
			playerprofiles.get(p).UploadStats();
			playerprofiles.remove(p);
		}
		e.setQuitMessage(LanguageTools.getQUIT_MESSAGE(p));
	}
}
