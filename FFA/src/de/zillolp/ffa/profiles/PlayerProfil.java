package de.zillolp.ffa.profiles;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.KitTools;
import de.zillolp.ffa.config.tools.ScoreboardTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.map.ArenaManager;
import de.zillolp.ffa.runnables.Manager;
import de.zillolp.ffa.stats.DatenManager;

public class PlayerProfil {
	private HashMap<Player, InventoryProfil> invprofiles;
	private final Player p;
	private String uuid;
	private ScoreboardTools scoreboardtools;
	private KitTools kittools;
	private boolean buildmode;
	private String arena;
	private boolean ingame;
	private Long kills;
	private Long deaths;
	private Long killstreak;

	public PlayerProfil(Player p) {
		this.invprofiles = Main.invprofiles;
		this.p = p;
		this.uuid = p.getUniqueId().toString();
		this.scoreboardtools = new ScoreboardTools(p);
		this.kittools = new KitTools(ArenaManager.active_arena);
		this.buildmode = false;
		this.arena = "";
		this.ingame = false;
		this.kills = DatenManager.getKills(uuid);
		this.deaths = DatenManager.getDeaths(uuid);
		this.killstreak = 0L;
		if (!(invprofiles.containsKey(p))) {
			invprofiles.put(p, new InventoryProfil());
		}
		if (ConfigTools.getScoreboard()) {
			Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {

				@Override
				public void run() {
					Manager.setScoreboard(p);
				}
			}, 5);
		}
	}

	public ScoreboardTools getScoreboardtools() {
		return scoreboardtools;
	}

	public KitTools getKittools() {
		return kittools;
	}

	public boolean getBuildmode() {
		return buildmode;
	}

	public String getArena() {
		return arena;
	}

	public boolean getIngame() {
		return ingame;
	}

	public Long getKills() {
		return kills;
	}

	public Long getDeaths() {
		return deaths;
	}

	public Long getKillstreak() {
		return killstreak;
	}

	public void setKittools(KitTools kittools) {
		this.kittools = kittools;
	}

	public void setBuildmode(boolean buildmode) {
		this.buildmode = buildmode;
	}

	public void setArena(String arena) {
		this.arena = arena;
	}

	public void setIngame(boolean ingame) {
		this.ingame = ingame;
	}

	public void setKillstreak(Long killstreak) {
		this.killstreak = killstreak;
	}

	public void addKills(Long kills) {
		this.kills = this.kills + kills;
	}

	public void addDeaths(Long deaths) {
		this.deaths = this.deaths + deaths;
	}

	public void addKillstreak(Long killstreak) {
		this.killstreak = this.killstreak + killstreak;
	}

	public void reloadProfil() {
		if (invprofiles.containsKey(p)) {
			invprofiles.get(p).reloadInventorys();
		}
	}

	public void UploadStats() {
		DatenManager.setKills(uuid, kills);
		DatenManager.setDeaths(uuid, deaths);
		if (invprofiles.containsKey(p)) {
			invprofiles.remove(p);
		}
	}
}
