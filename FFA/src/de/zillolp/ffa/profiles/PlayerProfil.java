package de.zillolp.ffa.profiles;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.KitTools;
import de.zillolp.ffa.config.tools.ScoreboardTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.runnables.Manager;
import de.zillolp.ffa.stats.DatenManager;

public class PlayerProfil {
	private HashMap<Player, InventoryProfil> invprofiles;
	private final Player p;
	private String uuid;
	private ScoreboardTools scoreboardtools;
	private KitTools kittools;
	private boolean joined;
	private boolean buildmode;
	private boolean setsign;
	private String arena;
	private boolean ingame;
	private Long kills;
	private Long deaths;
	private Long killstreak;
	private ItemStack[] inv;
	private ItemStack[] armor;
	private int Level;
	private float Exp;
	private int FoodLevel;
	private int Health;
	private GameMode Gamemode;

	public PlayerProfil(Player p) {
		this.invprofiles = Main.getInstance().invprofiles;
		this.p = p;
		this.uuid = p.getUniqueId().toString();
		this.scoreboardtools = new ScoreboardTools(p);
		this.kittools = new KitTools(Main.getInstance().getArenaManager().activeArena);
		if (ConfigTools.getBungeecord()) {
			this.joined = true;
		} else {
			this.joined = false;
		}
		this.buildmode = false;
		this.setsign = false;
		this.arena = "";
		this.ingame = false;
		this.kills = DatenManager.getKills(uuid);
		this.deaths = DatenManager.getDeaths(uuid);
		this.killstreak = 0L;
		this.inv = new ItemStack[] {};
		this.armor = null;
		this.Level = 0;
		this.Exp = 0;
		this.FoodLevel = 20;
		this.Health = 20;
		this.Gamemode = ConfigTools.getGamemode();
		if (!(invprofiles.containsKey(p))) {
			invprofiles.put(p, new InventoryProfil());
		}
		if (ConfigTools.getScoreboard() && joined) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

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

	public boolean getJoined() {
		return joined;
	}

	public boolean getBuildmode() {
		return buildmode;
	}
	
	public boolean getSetsign() {
		return setsign;
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

	public ItemStack[] getInv() {
		return inv;
	}

	public ItemStack[] getArmor() {
		return armor;
	}

	public int getLevel() {
		return Level;
	}

	public float getExp() {
		return Exp;
	}

	public int getFoodLevel() {
		return FoodLevel;
	}

	public int getHealth() {
		return Health;
	}

	public GameMode getGamemode() {
		return Gamemode;
	}

	public void setKittools(KitTools kittools) {
		this.kittools = kittools;
	}

	public void setJoined(boolean joined) {
		this.joined = joined;
	}

	public void setBuildmode(boolean buildmode) {
		this.buildmode = buildmode;
	}
	
	public void setSetsign(boolean setsign) {
		this.setsign = setsign;
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

	public void setInv(ItemStack[] inv) {
		this.inv = inv;
	}

	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}

	public void setLevel(int level) {
		Level = level;
	}

	public void setExp(float exp) {
		Exp = exp;
	}

	public void setFoodLevel(int foodLevel) {
		FoodLevel = foodLevel;
	}

	public void setHealth(int health) {
		Health = health;
	}

	public void setGamemode(GameMode gamemode) {
		Gamemode = gamemode;
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
