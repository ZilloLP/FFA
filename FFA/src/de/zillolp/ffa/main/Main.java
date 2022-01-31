package de.zillolp.ffa.main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.zillolp.ffa.bstats.Metrics;
import de.zillolp.ffa.commands.BuildCommand;
import de.zillolp.ffa.commands.FFACommand;
import de.zillolp.ffa.config.ConfigCreation;
import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.KitTools;
import de.zillolp.ffa.config.tools.LanguageTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.config.tools.PermissionTools;
import de.zillolp.ffa.listeners.AnvilEventNew;
import de.zillolp.ffa.listeners.AnvilEventOld;
import de.zillolp.ffa.listeners.ArenaEditListener;
import de.zillolp.ffa.listeners.BlockListener;
import de.zillolp.ffa.listeners.BlockPlaceListener;
import de.zillolp.ffa.listeners.GameChangeListener;
import de.zillolp.ffa.listeners.PlayerConnectionListener;
import de.zillolp.ffa.listeners.RespawnListener;
import de.zillolp.ffa.map.ArenaManager;
import de.zillolp.ffa.placeholderapi.Expansion;
import de.zillolp.ffa.profiles.InventoryProfil;
import de.zillolp.ffa.profiles.PlayerProfil;
import de.zillolp.ffa.runnables.Manager;
import de.zillolp.ffa.stats.DatenManager;
import de.zillolp.ffa.utils.InventorySetter;
import de.zillolp.ffa.xclasses.XMaterial;

public class Main extends JavaPlugin {
	
	public boolean disabled;
	public HashMap<Player, InventoryProfil> invprofiles;
	public HashMap<Player, PlayerProfil> playerprofiles;
	
	private static Main instance;
	private ArenaManager arenaManager;
	private InventorySetter inventorySetter;
	private ConfigCreation configCreation;
	
	@Override
	public void onEnable() {
		instance = this;
		new Metrics(this, 12256);
		if (register()) {
			init(Bukkit.getPluginManager());
			Bukkit.getConsoleSender()
					.sendMessage(
							"\r\n"  
							+ "§c                   ______  ______  _____ \r\n"
							+ "§c                 (______)(______)(_____) \r\n"
							+ "§c                 (_)__   (_)__  (_)___(_)\r\n"
							+ "§c                 (____)  (____) (_______)\r\n"
							+ "§c                 (_)     (_)    (_)   (_)\r\n"
							+ "§c                 (_)     (_)    (_)   (_)\r\n" 
							+ ""
							);
			Bukkit.getConsoleSender().sendMessage("§7-----------------------");
			Bukkit.getConsoleSender().sendMessage("§cFFA §e" + getDescription().getVersion());
			Bukkit.getConsoleSender().sendMessage("§7Developed by §bZilloLP");
			Bukkit.getConsoleSender().sendMessage("§7-----------------------");
		} else {
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

	@Override
	public void onDisable() {
		disabled = true;
		if (DatenManager.getConnected()) {
			BlockPlaceListener.replaceAll();
			uploadPlayers();
			if (!(DatenManager.getStatus())) {
				DatenManager.getSqlite().close();
			} else {
				DatenManager.getMysql().close();
			}
		}
		if (ConfigTools.getEnglish()) {
			Bukkit.getConsoleSender().sendMessage(LanguageTools.getPREFIX() + "§cThe plugin has stoped");
		} else {
			Bukkit.getConsoleSender().sendMessage(LanguageTools.getPREFIX() + "§cDas Plugin wurde beendet.");
		}
	}

	public static Main getInstance() {
		return instance;
	}
	
	public ArenaManager getArenaManager() {
		return arenaManager;
	}

	public InventorySetter getInventorySetter() {
		return inventorySetter;
	}
	
	public ConfigCreation getConfigCreation() {
		return configCreation;
	}
	
	private boolean register() {
		configCreation = new ConfigCreation();
		new ConfigTools();
		new LanguageTools();
		new PermissionTools();
		new DatenManager();
		return DatenManager.getConnected();
	}

	private void init(PluginManager pm) {
		disabled = false;
		invprofiles = new HashMap<>();
		playerprofiles = new HashMap<>();
		inventorySetter = new InventorySetter();
		arenaManager = new ArenaManager();
		KitTools.loadKits();
		if (ConfigTools.getScoreboard() || ConfigTools.getActionbar()) {
			new Manager();
		}
		if (XMaterial.supports(9)) {
			pm.registerEvents(new AnvilEventNew(), this);
		} else {
			pm.registerEvents(new AnvilEventOld(), this);
		}
		pm.registerEvents(new ArenaEditListener(), this);
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new BlockPlaceListener(), this);
		pm.registerEvents(new GameChangeListener(), this);
		pm.registerEvents(new PlayerConnectionListener(), this);
		pm.registerEvents(new RespawnListener(), this);
		getCommand("build").setExecutor(new BuildCommand());
		getCommand("ffa").setExecutor(new FFACommand());
		createPlayers();
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new Expansion().register();
		}
	}

	public void reload() {
		uploadPlayers();
		BlockPlaceListener.replaceAll();
		Manager.stop();
		new ConfigTools();
		new LanguageTools();
		new PermissionTools();
		new InventorySetter();
		invprofiles.clear();
		playerprofiles.clear();
		KitTools.loadKits();
		if (ConfigTools.getScoreboard() || ConfigTools.getActionbar()) {
			new Manager();
		}
		arenaManager.refresh();
		createPlayers();
	}

	private void uploadPlayers() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (playerprofiles.containsKey(all)) {
				PlayerProfil playerprofil = playerprofiles.get(all);
				if (playerprofil.getJoined()) {
					all.getInventory().setContents(playerprofil.getInv());
					all.getInventory().setArmorContents(playerprofil.getArmor());
					all.setLevel(playerprofil.getLevel());
					all.setExp(playerprofil.getExp());
					all.setFoodLevel(playerprofil.getFoodLevel());
					all.setHealth(playerprofil.getHealth());
					all.setGameMode(playerprofil.getGamemode());
				}
				playerprofil.UploadStats();
			}
		}
	}

	private void createPlayers() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			DatenManager.createPlayer(all);
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {
				@Override
				public void run() {
					if (!(playerprofiles.containsKey(all))) {
						playerprofiles.put(all, new PlayerProfil(all));
					}
				}
			}, 5);
			if (ConfigTools.getBungeecord()) {
				String arena = arenaManager.activeArena;
				if (arena != null) {
					LocationTools locationtools = new LocationTools(arena);
					all.teleport(locationtools.loadLocation("Spawn"));
				}
			}
		}
	}
}
