package de.zillolp.ffa.config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;

import de.zillolp.ffa.utils.ConfigUtil;
import de.zillolp.ffa.xclasses.XMaterial;

@SuppressWarnings("unused")
public class ConfigCreation {
	private LinkedHashMap<String, String> string_defaults = new LinkedHashMap<>();
	private LinkedHashMap<String, Boolean> boolean_defaults = new LinkedHashMap<>();
	private LinkedHashMap<String, Integer> integer_defaults = new LinkedHashMap<>();

	public ConfigCreation() {
		createConfigfile();
		createPermissionsfile();
		createLanguagefile();
		createScoreboardfile();
		createKitsfile();
		createLocationsfile();
	}

	private void createConfigfile() {
		ConfigUtil configutil = new ConfigUtil(new File("plugins/FFA/config.yml"));
		if (!(configutil.isConfig())) {
			configutil.createConfig();
			configutil.addComment("Saves all stats in the MySQL database");
			configutil.addDefaultBoolean("MySQL", false);
			configutil.addComment("Everything for the setup is in English");
			configutil.addDefaultBoolean("English", false);
			configutil.addComment("Set this to false if you want no scoreboard to be displayed");
			configutil.addDefaultBoolean("Scoreboard", true);
			configutil.addComment("Set this to false if you want no actionbar to be displayed");
			configutil.addDefaultBoolean("Actionbar", true);
			configutil.addComment("Set this to false if you want, that items can loose durability");
			configutil.addDefaultBoolean("Unbreakable", true);
			configutil.addComment("Default GameMode for players");
			configutil.addComment("Only works if no other plugin sets the gamemode");
			configutil.addDefaultString("Gamemode", "SURVIVAL");
			configutil.addComment("Set this to false if you want no mapchange");
			configutil.addComment("If Mapchange is false, the arena created first is always played");
			configutil.addDefaultBoolean("Mapchange", true);
			configutil.addComment("Mapchange sets the time until a new map has been selected");
			configutil.addComment("The mapchange time is in minutes");
			configutil.addDefaultInteger("Mapchange Time", 30);
			configutil.addComment("Set the radius around the spawn where you cannot build");
			configutil.addDefaultInteger("Building Protection", 4);
			configutil.addComment("Sets the type of block before it becomes air");
			configutil.addDefaultString("Replace Type", XMaterial.REDSTONE_BLOCK.name());
			configutil.addComment("Time until it is replaced with replace type in seconds");
			configutil.addDefaultInteger("Replace Time", 4);
			configutil.addComment("Time until it is replaced with air in seconds");
			configutil.addDefaultInteger("Air Time", 7);
			configutil.addComment("Players cannot interact with the blocks on the list");
			string_defaults.put("Protected Blocks.1", XMaterial.TRAPPED_CHEST.name());
			string_defaults.put("Protected Blocks.2", XMaterial.CHEST.name());
			string_defaults.put("Protected Blocks.3", XMaterial.FURNACE.name());
			string_defaults.put("Protected Blocks.4", XMaterial.ENDER_CHEST.name());
			string_defaults.put("Protected Blocks.5", XMaterial.DISPENSER.name());
			string_defaults.put("Protected Blocks.6", XMaterial.DROPPER.name());
			string_defaults.put("Protected Blocks.7", XMaterial.HOPPER.name());
			configutil.addDefaultStrings(string_defaults);
			string_defaults.clear();
		}
	}

	private void createPermissionsfile() {
		ConfigUtil configutil = new ConfigUtil(new File("plugins/FFA/permissions.yml"));
		if (!(configutil.isConfig())) {
			configutil.createConfig();
			configutil.addComment("Permissions");
			string_defaults.put("ADMIN_PERMISSION", "ffa.admin");
			configutil.addDefaultStrings(string_defaults);
			string_defaults.clear();
		}
	}

	private void createLanguagefile() {
		ConfigUtil configutil = new ConfigUtil(new File("plugins/FFA/language.yml"));
		if (!(configutil.isConfig())) {
			configutil.createConfig();
			configutil.addComment("Normal Tags %Ae%, %ae%, %Oe%, %oe%, %Ue%, %ue%, %sz%");
			configutil.addComment("Special Tags %>%, %<%, %*%, %->%, %<3%, &");
			configutil.addComment("Basic messages");
			string_defaults.put("PREFIX", "&7[&cFFA&7] ");
			string_defaults.put("NO_PERMISSION", "&cDazu hast du keine Rechte!");
			string_defaults.put("ONLY_PLAYER", "&cDu musst ein Spieler sein!");
			string_defaults.put("JOIN_MESSAGE", "&8+ &a%name%");
			string_defaults.put("QUIT_MESSAGE", "&8- &c%name%");
			string_defaults.put("BUILD_PROTECTION", "&cDu darfst nich so nah am Spawn bauen!");
			configutil.addDefaultStrings(string_defaults);
			string_defaults.clear();
			configutil.addComment("Fight messages");
			string_defaults.put("PLAYER_DIED", "&7Du bist &cgestorben&7!");
			string_defaults.put("KILLED_PLAYER", "&7Du hast &a%name% &7mit &c%<3%%hearts% &7get%oe%tet.");
			string_defaults.put("DIED_BY_PLAYER", "&7Du wurdest von &a%name% &7mit &c%<3%%hearts% &7get%oe%tet.");
			string_defaults.put("KILLSTREAK_WIN",
					"&7Der Spieler &a%name% &7hat eine Killstreak von &a%streak% &7Kills!");
			string_defaults.put("KILLSTREAK_LOSE", "&7Du hast deine &a%streak%er &7Killstreak &cverloren&7!");
			configutil.addDefaultStrings(string_defaults);
			string_defaults.clear();
			configutil.addComment("Mapchange messages");
			string_defaults.put("ARENA_CHANGE_SECONDS", "&7Arena wechsel in &b%seconds% &7Sekunden.");
			string_defaults.put("ARENA_CHANGE_SECOND", "&7Arena wechsel in &b%seconds% &7Sekunde.");
			string_defaults.put("ARENA_CHANGED", "&7Die neue Arena ist &b%map%&7.");
			configutil.addDefaultStrings(string_defaults);
			string_defaults.clear();
		}
	}

	private void createScoreboardfile() {
		ConfigUtil configutil = new ConfigUtil(new File("plugins/FFA/scoreboard.yml"));
		if (!(configutil.isConfig())) {
			configutil.createConfig();
			configutil.addComment("Shows the hearts of a player above his head");
			configutil.addDefaultBoolean("Show Hearts", true);
			configutil.addDefaultString("Hearts", "&c%<3%");
			configutil.addComment("Scoreboard Options");
			string_defaults.put("Scoreboard.Title", "&f%>% &lDeinServer.net");
			string_defaults.put("Scoreboard.Lines.1", "%blank%");
			string_defaults.put("Scoreboard.Lines.2", "&fKills:");
			string_defaults.put("Scoreboard.Lines.3", "&e%kills%");
			string_defaults.put("Scoreboard.Lines.4", "%blank%");
			string_defaults.put("Scoreboard.Lines.5", "&fMap:");
			string_defaults.put("Scoreboard.Lines.6", "&d%map%");
			string_defaults.put("Scoreboard.Lines.7", "%blank%");
			string_defaults.put("Scoreboard.Lines.8", "&fDeaths:");
			string_defaults.put("Scoreboard.Lines.9", "&c%deaths%");
			string_defaults.put("Scoreboard.Lines.10", "%blank%");
			string_defaults.put("Scoreboard.Lines.11", "&fKillstreak:");
			string_defaults.put("Scoreboard.Lines.12", "&9%streak%");
			string_defaults.put("Scoreboard.Lines.13", "%blank%");
			string_defaults.put("Actionbar", "&7%*% &7Teams sind %teams% &7| Mapchange in &e%time% &7%*%");
			configutil.addDefaultStrings(string_defaults);
			string_defaults.clear();
		}
	}

	private void createKitsfile() {
		ConfigUtil configutil = new ConfigUtil(new File("plugins/FFA/kits.yml"));
		if (!(configutil.isConfig())) {
			configutil.createConfig();
			configutil.addComment("Kits");
		}
	}

	private void createLocationsfile() {
		ConfigUtil configutil = new ConfigUtil(new File("plugins/FFA/locations.yml"));
		if (!(configutil.isConfig())) {
			configutil.createConfig();
			configutil.addComment("Locations");
		}
	}
}
