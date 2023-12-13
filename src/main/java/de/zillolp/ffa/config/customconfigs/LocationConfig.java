package de.zillolp.ffa.config.customconfigs;

import de.zillolp.ffa.FFA;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedList;

public class LocationConfig extends CustomConfig {
    private final String root;

    public LocationConfig(FFA plugin, String name) {
        super(plugin, name);
        this.root = "locations.";
    }

    public boolean isLocation(String locationName) {
        return fileConfiguration.contains(root + locationName) && getLocation(locationName) != null;
    }

    public boolean isMap(String mapName) {
        return fileConfiguration.contains(root + "Maps." + mapName);
    }

    public Location getLocation(String locationName) {
        String section = root + locationName;
        String worldName = fileConfiguration.getString(section + ".world");
        if (worldName == null) {
            return null;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }
        double x = fileConfiguration.getDouble(section + ".x");
        double y = fileConfiguration.getDouble(section + ".y");
        double z = fileConfiguration.getDouble(section + ".z");
        float yaw = (float) fileConfiguration.getDouble(section + ".yaw");
        float pitch = (float) fileConfiguration.getDouble(section + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    public String getBuilderName(String mapName) {
        return fileConfiguration.getString(root + "Maps." + mapName + ".Builder", "N/A");
    }

    public String getKitItems(String mapName) {
        return fileConfiguration.getString(root + "Maps." + mapName + ".Items", "");
    }

    public String getArmor(String mapName) {
        return fileConfiguration.getString(root + "Maps." + mapName + ".Armor", "");
    }

    public int getRadius(String mapName) {
        return fileConfiguration.getInt(root + "Maps." + mapName + ".Radius", 5);
    }

    public boolean isCuboid(String mapName) {
        return fileConfiguration.getBoolean(root + "Maps." + mapName + ".Cuboid", false);
    }

    public LinkedList<Location> getLocations(String section) {
        LinkedList<Location> locations = new LinkedList<>();
        if (!(fileConfiguration.isConfigurationSection(root + section))) {
            return locations;
        }
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection(root + section);
        if (configurationSection == null) {
            return locations;
        }
        for (String locationName : configurationSection.getKeys(false)) {
            locations.add(getLocation(section + "." + locationName));
        }
        return locations;
    }

    public void saveLocation(String locationName, Location location) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }
        String section = root + locationName;
        fileConfiguration.set(section + ".world", world.getName());
        fileConfiguration.set(section + ".x", location.getX());
        fileConfiguration.set(section + ".y", location.getY());
        fileConfiguration.set(section + ".z", location.getZ());
        fileConfiguration.set(section + ".yaw", location.getYaw());
        fileConfiguration.set(section + ".pitch", location.getPitch());
        save();
    }

    public void saveMap(String mapName, String builderName) {
        fileConfiguration.set(root + "Maps." + mapName + ".Builder", builderName);
        save();
    }

    public void saveKitItems(String mapName, String kitItems) {
        fileConfiguration.set(root + "Maps." + mapName + ".Items", kitItems);
        save();
    }

    public void saveKitArmor(String mapName, String armor) {
        fileConfiguration.set(root + "Maps." + mapName + ".Armor", armor);
        save();
    }

    public void saveRadius(String mapName, int radius) {
        fileConfiguration.set(root + "Maps." + mapName + ".Radius", radius);
        save();
    }

    public void saveCuboid(String mapName, boolean isCuboid) {
        fileConfiguration.set(root + "Maps." + mapName + ".Cuboid", isCuboid);
        save();
    }

    public void removeLocation(String locationName) {
        fileConfiguration.set(root + locationName, null);
        save();
    }
}
