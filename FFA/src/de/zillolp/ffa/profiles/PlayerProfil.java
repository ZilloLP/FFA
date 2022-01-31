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
	private HashMap<Player, InventoryProfil> invProfiles;
	private final Player player;
	private String uuid;
	private ScoreboardTools scoreboardTools;
	private KitTools kitTools;
	private boolean joined;
	private boolean buildMode;
	private boolean setSign;
	private String arena;
	private boolean ingame;
	private Long kills;
	private Long deaths;
	private Long killstreak;
	private ItemStack[] inventory;
	private ItemStack[] armor;
	private int level;
	private float exp;
	private int foodLevel;
	private int health;
	private GameMode gamemode;

	public PlayerProfil(Player p) {
		this.invProfiles = Main.getInstance().invprofiles;
		this.player = p;
		this.uuid = p.getUniqueId().toString();
		this.scoreboardTools = new ScoreboardTools(p);
		this.kitTools = new KitTools(Main.getInstance().getArenaManager().activeArena);
		if (ConfigTools.getBungeecord()) {
			this.joined = true;
		} else {
			this.joined = false;
		}
		this.buildMode = false;
		this.setSign = false;
		this.arena = "";
		this.ingame = false;
		this.kills = DatenManager.getKills(uuid);
		this.deaths = DatenManager.getDeaths(uuid);
		this.killstreak = 0L;
		this.inventory = new ItemStack[] {};
		this.armor = null;
		this.level = 0;
		this.exp = 0;
		this.foodLevel = 20;
		this.health = 20;
		this.gamemode = ConfigTools.getGamemode();
		
		if (!(invProfiles.containsKey(p))) {
			invProfiles.put(p, new InventoryProfil());
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
		return scoreboardTools;
	}

	public KitTools getKittools() {
		return kitTools;
	}

	public boolean getJoined() {
		return joined;
	}

	public boolean getBuildmode() {
		return buildMode;
	}
	
	public boolean getSetsign() {
		return setSign;
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
		return inventory;
	}

	public ItemStack[] getArmor() {
		return armor;
	}

	public int getLevel() {
		return level;
	}

	public float getExp() {
		return exp;
	}

	public int getFoodLevel() {
		return foodLevel;
	}

	public int getHealth() {
		return health;
	}

	public GameMode getGamemode() {
		return gamemode;
	}

	public void setKittools(KitTools kittools) {
		this.kitTools = kittools;
	}

	public void setJoined(boolean joined) {
		this.joined = joined;
	}

	public void setBuildmode(boolean buildmode) {
		this.buildMode = buildmode;
	}
	
	public void setSetsign(boolean setsign) {
		this.setSign = setsign;
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

	public void setInv(ItemStack[] inventory) {
		this.inventory = inventory;
	}

	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setExp(float exp) {
		this.exp = exp;
	}

	public void setFoodLevel(int foodLevel) {
		this.foodLevel = foodLevel;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setGamemode(GameMode gamemode) {
		this.gamemode = gamemode;
	}

	public void addKills(Long kills) {
		this.kills += kills;
	}

	public void addDeaths(Long deaths) {
		this.deaths += deaths;
	}

	public void addKillstreak(Long killstreak) {
		this.killstreak += killstreak;
	}

	public void reloadProfil() {
		if (invProfiles.containsKey(player)) {
			invProfiles.get(player).reloadInventorys();
		}
	}

	public void UploadStats() {
		DatenManager.setKills(uuid, kills);
		DatenManager.setDeaths(uuid, deaths);
		
		if (invProfiles.containsKey(player)) {
			invProfiles.remove(player);
		}
		
	}
}
