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
import de.zillolp.ffa.profiles.PlayerProfil;
import de.zillolp.ffa.stats.DatenManager;

public class PlayerConnectionListener implements Listener {
	private HashMap<Player, PlayerProfil> playerprofiles = Main.getInstance().playerprofiles;

	@EventHandler
	public void on(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		DatenManager.createPlayer(player);
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				PlayerProfil profil = new PlayerProfil(player);
				if (playerprofiles.containsKey(player)) {
					playerprofiles.replace(player, profil);
				} else {
					playerprofiles.put(player, profil);
				}
			}
		}, 5);
		if (ConfigTools.getBungeecord()) {
			e.setJoinMessage(LanguageTools.getJOIN_MESSAGE(player));

			String arena = Main.getInstance().getArenaManager().active_arena;
			if (arena != null) {
				LocationTools locationtools = new LocationTools(arena);
				player.teleport(locationtools.loadLocation("Spawn"));
			}
		}
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (playerprofiles.containsKey(p)) {
			PlayerProfil playerprofil = playerprofiles.get(p);
			if (ConfigTools.getBungeecord()) {
				e.setQuitMessage(LanguageTools.getQUIT_MESSAGE(p));
			}
			if (playerprofil.getJoined()) {
				p.getInventory().setContents(playerprofil.getInv());
				p.getInventory().setArmorContents(playerprofil.getArmor());
				p.setLevel(playerprofil.getLevel());
				p.setExp(playerprofil.getExp());
				p.setFoodLevel(playerprofil.getFoodLevel());
				p.setHealth(playerprofil.getHealth());
				p.setGameMode(playerprofil.getGamemode());
			}
			playerprofil.UploadStats();
			playerprofiles.remove(p);
		}
	}
}
