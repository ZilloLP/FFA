package de.zillolp.ffa.config.tools;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.utils.ConfigUtil;

public class LocationTools {
	private ConfigUtil configutil = Main.getInstance().getConfigCreation().getManager().getNewConfig("locations.yml");
	private String arena;
	private Location loc;

	public LocationTools(String arena) {
		this.arena = "Arenas." + arena;
	}

	public LocationTools(String arena, Location loc) {
		this.arena = "Arenas." + arena;
		this.loc = loc;
	}

	public boolean isArena() {
		boolean isArena = false;
		if (configutil.getString(arena) != null) {
			isArena = true;
		}
		return isArena;
	}

	public boolean isPlayable() {
		boolean isPlayable = false;
		if (isArena()) {
			if (isLocation("Spawn") && isLocation("Bottomcorner") && isLocation("Uppercorner")) {
				isPlayable = true;
			}
		}
		return isPlayable;
	}

	public boolean isLocation(String name) {
		boolean isLocation = false;
		if (configutil.getString(arena + "." + name) != null) {
			isLocation = true;
		}
		return isLocation;
	}

	public void saveArena() {
		configutil.set(arena + ".Teams", false);
		configutil.set(arena + ".Build", true);
	}

	public void saveLocation(String name) {
		configutil.set(arena + "." + name + ".World", loc.getWorld().getName());
		configutil.set(arena + "." + name + ".X", loc.getX());
		configutil.set(arena + "." + name + ".Y", loc.getY());
		configutil.set(arena + "." + name + ".Z", loc.getZ());
		configutil.set(arena + "." + name + ".Yaw", loc.getYaw());
		configutil.set(arena + "." + name + ".Pitch", loc.getPitch());
	}

	public void setTeams(boolean teams) {
		configutil.set(arena + ".Teams", teams);
	}

	public void setBuild(boolean build) {
		configutil.set(arena + ".Build", build);
	}

	public Location loadLocation(String name) {
		if (configutil.getString(arena + "." + name + ".World") != null
				&& Bukkit.getWorlds().contains(Bukkit.getWorld(configutil.getString(arena + "." + name + ".World")))) {
			World world = Bukkit.getWorld(configutil.getString(arena + "." + name + ".World"));
			double x = configutil.getDouble(arena + "." + name + ".X");
			double y = configutil.getDouble(arena + "." + name + ".Y");
			double z = configutil.getDouble(arena + "." + name + ".Z");
			float yaw = (float) configutil.getDouble(arena + "." + name + ".Yaw");
			float pitch = (float) configutil.getDouble(arena + "." + name + ".Pitch");
			return new Location(world, x, y, z, yaw, pitch);
		} else {
			return null;
		}
	}

	public boolean getTeams() {
		return configutil.getBoolean(arena + ".Teams");
	}

	public boolean getBuild() {
		return configutil.getBoolean(arena + ".Build");
	}

	public void renameArena(String name) {
		ConfigurationSection section = configutil.getConfig().getConfigurationSection(arena);
		if (section != null) {
			for (String key : section.getKeys(false)) {
				Object value = configutil.getConfig().get(arena + "." + key);
				if (value != null) {
					configutil.getConfig().set("Arenas." + name + "." + key, value);
				} else {
					continue;
				}
			}
			resetArena();
		}
	}

	public void resetArena() {
		ConfigurationSection section = configutil.getConfig().getConfigurationSection(arena);
		if (section != null) {
			for (String key : section.getKeys(false)) {
				if (configutil.getString(arena + "." + key) != null) {
					configutil.set(arena + "." + key, null);
				} else {
					continue;
				}
			}
		}
		configutil.set(arena, null);
	}
}
