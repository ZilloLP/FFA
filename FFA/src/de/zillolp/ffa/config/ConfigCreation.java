package de.zillolp.ffa.config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;

import de.zillolp.ffa.utils.ConfigUtil;
import de.zillolp.ffa.utils.ConfigManager;
import de.zillolp.ffa.xclasses.XMaterial;

@SuppressWarnings("unused")
public class ConfigCreation {
	private ConfigManager manager;
	private String[] header = { "FFA" };
	private LinkedHashMap<String, Object> defaults = new LinkedHashMap<>();

	public ConfigCreation() {
		manager = new ConfigManager();
		createConfigfile();
		createPermissionsfile();
		createLanguagefile();
		createScoreboardfile();
		createKitsfile();
		createLocationsfile();
	}

	public ConfigManager getManager() {
		return manager;
	}
	
	private void createConfigfile() {
		ConfigUtil configutil = manager.getNewConfig("config.yml", header);
//		configutil.setDefault("Bungeecord", false, "If you are using bungeecord set this to true");
		configutil.setDefault("MySQL", false, "Saves all stats in the MySQL database");
		configutil.setDefault("English", false, "Everything for the setup is in English");
		configutil.setDefault("Scoreboard", true, "Set this to false if you want no scoreboard to be displayed");
		configutil.setDefault("Actionbar", true, "Set this to false if you want no actionbar to be displayed");
		configutil.setDefault("Unbreakable", true, "Set this to false if you want, that items can loose durability");
		configutil.setDefault("Gamemode", "SURVIVAL",
				new String[] { "Default GameMode for players", "Only works if no other plugin sets the gamemode" });
		configutil.setDefault("Mapchange", true, new String[] { "Set this to false if you want no mapchange",
				"If Mapchange is false, the arena created first is always played" });
		configutil.setDefault("Mapchange Time", 30, new String[] { "Mapchange sets the time until a new map has been selected",
				"The mapchange time is in minutes" });
		configutil.setDefault("Falldamage", true, "Set this to false if you want the player to get no falldamage");
		configutil.setDefault("Building Protection", 4, "Set the radius around the spawn where you cannot build");
		configutil.setDefault("Replace Type", XMaterial.REDSTONE_BLOCK.name(), "Sets the type of block before it becomes air");
		configutil.setDefault("Replace Time", 4, "Time until it is replaced with replace type in seconds");
		configutil.setDefault("Air Time", 7, "Time until it is replaced with air in seconds");
		configutil.setDefault("Infinite blocks", true, "The block stacks are infinite");
		configutil.setDefault("Protected Blocks.1", XMaterial.TRAPPED_CHEST.name(),
				"Players cannot interact with the blocks on the list");
		defaults.put("Protected Blocks.2", XMaterial.CHEST.name());
		defaults.put("Protected Blocks.3", XMaterial.FURNACE.name());
		defaults.put("Protected Blocks.4", XMaterial.ENDER_CHEST.name());
		defaults.put("Protected Blocks.5", XMaterial.DISPENSER.name());
		defaults.put("Protected Blocks.6", XMaterial.DROPPER.name());
		defaults.put("Protected Blocks.7", XMaterial.HOPPER.name());
		configutil.setDefault(defaults);
		defaults.clear();
		configutil.saveConfig();
	}

	private void createPermissionsfile() {
		ConfigUtil configutil = manager.getNewConfig("permissions.yml", header);
		configutil.setDefault("ADMIN_PERMISSION", "ffa.admin", "Permissions");
		configutil.saveConfig();
	}

	private void createLanguagefile() {
		ConfigUtil configutil = manager.getNewConfig("language.yml", header);
		configutil.setDefault("PREFIX", "&7[&cFFA&7] ", new String[] { "Normal Tags %Ae%, %ae%, %Oe%, %oe%, %Ue%, %ue%, %sz%",
				"Special Tags %>%, %<%, %*%, %->%, %<3%, &", "Basic messages" });
		defaults.put("NO_PERMISSION", "&cDazu hast du keine Rechte!");
		defaults.put("ONLY_PLAYER", "&cDu musst ein Spieler sein!");
		defaults.put("JOIN_MESSAGE", "&8+ &a%name%");
		defaults.put("QUIT_MESSAGE", "&8- &c%name%");
		defaults.put("BUILD_PROTECTION", "&cDu darfst nich so nah am Spawn bauen!");
		configutil.setDefault(defaults);
		defaults.clear();

		configutil.setDefault("PLAYER_DIED", "&7Du bist &cgestorben&7!", "Fight messages");
		defaults.put("KILLED_PLAYER", "&7Du hast &a%name% &7mit &c%<3%%hearts% &7get%oe%tet.");
		defaults.put("DIED_BY_PLAYER", "&7Du wurdest von &a%name% &7mit &c%<3%%hearts% &7get%oe%tet.");
		defaults.put("KILLSTREAK_WIN", "&7Der Spieler &a%name% &7hat eine Killstreak von &a%streak% &7Kills!");
		defaults.put("KILLSTREAK_LOSE", "&7Du hast deine &a%streak%er &7Killstreak &cverloren&7!");
		configutil.setDefault(defaults);
		defaults.clear();

		configutil.setDefault("ARENA_CHANGE_SECONDS", "&7Arena wechsel in &b%seconds% &7Sekunden.", "Mapchange messages");
		defaults.put("ARENA_CHANGE_SECOND", "&7Arena wechsel in &b%seconds% &7Sekunde.");
		defaults.put("ARENA_CHANGED", "&7Die neue Arena ist &b%map%&7.");
		configutil.setDefault(defaults);
		defaults.clear();
		configutil.saveConfig();
	}

	private void createScoreboardfile() {
		ConfigUtil configutil = manager.getNewConfig("scoreboard.yml", header);
		configutil.setDefault("Show Hearts", true, "Shows the hearts of a player above his head");
		configutil.set("Hearts", "&c%<3%");
		configutil.setDefault("Scoreboard.Title", "&f%>% &lDeinServer.net", "Scoreboard Options");
		defaults.put("Scoreboard.Lines.1", "%blank%");
		defaults.put("Scoreboard.Lines.2", "&fKills:");
		defaults.put("Scoreboard.Lines.3", "&e%kills%");
		defaults.put("Scoreboard.Lines.4", "%blank%");
		defaults.put("Scoreboard.Lines.5", "&fMap:");
		defaults.put("Scoreboard.Lines.6", "&d%map%");
		defaults.put("Scoreboard.Lines.7", "%blank%");
		defaults.put("Scoreboard.Lines.8", "&fDeaths:");
		defaults.put("Scoreboard.Lines.9", "&c%deaths%");
		defaults.put("Scoreboard.Lines.10", "%blank%");
		defaults.put("Scoreboard.Lines.11", "&fKillstreak:");
		defaults.put("Scoreboard.Lines.12", "&9%streak%");
		defaults.put("Scoreboard.Lines.13", "%blank%");
		defaults.put("Actionbar", "&7%*% &7Teams sind %teams% &7| Mapchange in &e%time% &7%*%");
		configutil.setDefault(defaults);
		defaults.clear();
		configutil.saveConfig();
	}

	private void createKitsfile() {
		ConfigUtil configutil = manager.getNewConfig("kits.yml", header);
		configutil.saveConfig();
	}

	private void createLocationsfile() {
		ConfigUtil configutil = manager.getNewConfig("locations.yml", header);
		configutil.saveConfig();
	}
}
