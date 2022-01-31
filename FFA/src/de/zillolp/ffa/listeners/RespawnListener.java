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
import de.zillolp.ffa.profiles.PlayerProfil;

public class RespawnListener implements Listener {

	@EventHandler
	public void on(PlayerDeathEvent e) {
		Player player = e.getEntity().getPlayer();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(player);
		if (playerprofil != null) {
			if (!(playerprofil.getBuildmode()) && playerprofil.getIngame() && playerprofil.getJoined()) {
				String PREFIX = LanguageTools.getPREFIX();
				e.setDeathMessage(null);
				e.setDroppedExp(0);
				e.getDrops().clear();
				Respawn(player, 1);
				if (player.getKiller() != null) {
					Player killer = player.getKiller();
					PlayerProfil killerprofil = Main.getInstance().playerprofiles.get(killer);
					if (killerprofil != null) {
						killer.sendMessage(PREFIX + LanguageTools.getKILLED_PLAYER(player, killer));
						player.sendMessage(PREFIX + LanguageTools.getDIED_BY_PLAYER(player, killer));
						killerprofil.addKillstreak(1L);
						killerprofil.addKills(1L);
						Long killstreak = killerprofil.getKillstreak();
						if (killstreak == 3 || killstreak % 5 == 0) {
							Bukkit.broadcastMessage(PREFIX + LanguageTools.getKILLSTREAK_WIN(killer));
						}
						killer.setHealth(20);
					}
				} else {
					player.sendMessage(PREFIX + LanguageTools.getPLAYER_DIED());
				}
				Long killstreak = playerprofil.getKillstreak();
				if (killstreak >= 3) {
					player.sendMessage(PREFIX + LanguageTools.getKILLSTREAK_LOSE(player));
				}
				playerprofil.addDeaths(1L);
				playerprofil.setKillstreak(0L);
			}
		}
	}

	@EventHandler
	public void on(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(player);
		if (playerprofil != null) {
			if (!(playerprofil.getBuildmode()) && playerprofil.getIngame() && playerprofil.getJoined()) {
				playerprofil.setIngame(false);
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				player.setLevel(0);
				player.setExp(0);
				player.setFoodLevel(20);
				player.setHealth(20);
				String arena = Main.getInstance().getArenaManager().active_arena;
				if (arena != null) {
					LocationTools locationtools = new LocationTools(arena);
					Location loc = locationtools.loadLocation("Spawn");
					e.setRespawnLocation(loc);
				}
				for (Player all : Bukkit.getOnlinePlayers()) {
					all.hidePlayer(player);
					all.showPlayer(player);
					player.hidePlayer(all);
					player.showPlayer(all);
				}
			}
		}
	}

	private void Respawn(Player player, int Time) {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				player.spigot().respawn();
			}
		}, Time);
	}
}
