package de.zillolp.ffa.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import de.zillolp.ffa.config.tools.LanguageTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.map.ArenaManager;
import de.zillolp.ffa.profiles.PlayerProfil;

public class RespawnListener implements Listener {

	@EventHandler
	public void on(PlayerDeathEvent e) {
		Player p = e.getEntity().getPlayer();
		PlayerProfil playerprofil = Main.playerprofiles.get(p);
		if (playerprofil != null) {
			String PREFIX = LanguageTools.getPREFIX();
			e.setDeathMessage(null);
			e.setDroppedExp(0);
			e.getDrops().clear();
			Respawn(p, 1);
			if (p.getKiller() != null) {
				Player k = p.getKiller();
				PlayerProfil killerprofil = Main.playerprofiles.get(k);
				if (killerprofil != null) {
					k.sendMessage(PREFIX + LanguageTools.getKILLED_PLAYER(p, k));
					p.sendMessage(PREFIX + LanguageTools.getDIED_BY_PLAYER(p, k));
					killerprofil.addKillstreak(1L);
					killerprofil.addKills(1L);
					Long killstreak = killerprofil.getKillstreak();
					if (killstreak == 3 || killstreak % 5 == 0) {
						Bukkit.broadcastMessage(PREFIX + LanguageTools.getKILLSTREAK_WIN(k));
					}
					k.setHealth(20);
				}
			} else {
				p.sendMessage(PREFIX + LanguageTools.getPLAYER_DIED());
			}
			Long killstreak = playerprofil.getKillstreak();
			if (killstreak >= 3) {
				p.sendMessage(PREFIX + LanguageTools.getKILLSTREAK_LOSE(p));
			}
			playerprofil.addDeaths(1L);
			playerprofil.setKillstreak(0L);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void on(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		PlayerProfil playerprofil = Main.playerprofiles.get(p);
		if (playerprofil != null) {
			playerprofil.setIngame(false);
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			p.setLevel(0);
			p.setExp(0);
			p.setFoodLevel(20);
			p.setHealth(20);
			String arena = ArenaManager.active_arena;
			if (arena != null) {
				LocationTools locationtools = new LocationTools(arena);
				Location loc = locationtools.loadLocation("Spawn");
				e.setRespawnLocation(loc);
			}
			for (Player all : Bukkit.getOnlinePlayers()) {
				all.hidePlayer(p);
				all.showPlayer(p);
				p.hidePlayer(all);
				p.showPlayer(all);
			}
		}
	}

	private void Respawn(Player p, int Time) {
		Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {

			@Override
			public void run() {
				p.spigot().respawn();
			}
		}, Time);
	}
}
