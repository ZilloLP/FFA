package de.zillolp.ffa.map;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.LocationConfig;
import de.zillolp.ffa.serializer.Base64Serializer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.logging.Level;

public class Map {
    private final FFA plugin;
    private final LocationConfig locationConfig;
    private final MapManager mapManager;
    private final Base64Serializer base64Serializer;
    private final String mapName;
    private String builderName;
    private Location spawnLocation;
    private Location protectionLocation;
    private Location topLocation;
    private ItemStack[] items;
    private ItemStack[] armor;
    private int radius;
    private boolean isCuboid;

    public Map(FFA plugin, String mapName) {
        this.plugin = plugin;
        this.locationConfig = plugin.getLocationConfig();
        this.mapManager = plugin.getMapManager();
        this.base64Serializer = plugin.getBase64Serializer();
        this.mapName = mapName;
    }

    public boolean isExisting() {
        return locationConfig.isMap(mapName);
    }

    public boolean isPlayable() {
        return builderName != null && spawnLocation != null && items != null && armor != null;
    }

    public boolean isLocationInProtection(Location location) {
        if (protectionLocation == null) {
            return false;
        }
        if (!(isCuboid)) {
            return protectionLocation.distance(location) <= radius;
        }
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        int pX = protectionLocation.getBlockX();
        int pY = protectionLocation.getBlockY();
        int pZ = protectionLocation.getBlockZ();
        return (Math.max(x, pX) - Math.min(x, pX) <= radius) && (Math.max(y, pY) - Math.min(y, pY) <= radius) && (Math.max(z, pZ) - Math.min(z, pZ) <= radius);
    }

    public void create(String builderName) {
        this.builderName = builderName;
        this.radius = 5;
        this.isCuboid = false;
        locationConfig.saveMap(mapName, builderName);
        mapManager.getMaps().put(mapName, this);
    }

    public void load() {
        String path = "Maps." + mapName;
        this.builderName = locationConfig.getBuilderName(mapName);
        this.spawnLocation = locationConfig.getLocation(path + ".Spawn");
        this.protectionLocation = new Location(spawnLocation.getWorld(), spawnLocation.getBlockX(), spawnLocation.getBlockY(), spawnLocation.getBlockZ());
        this.topLocation = locationConfig.getLocation(path + ".Top");
        try {
            this.items = base64Serializer.itemStackArrayFromBase64(locationConfig.getKitItems(mapName));
            this.armor = base64Serializer.itemStackArrayFromBase64(locationConfig.getArmor(mapName));
        } catch (IOException ignored) {
            plugin.getLogger().log(Level.WARNING, "Error during serialization of items");
        }
        this.radius = locationConfig.getRadius(mapName);
        this.isCuboid = locationConfig.isCuboid(mapName);
    }

    public String getMapName() {
        return mapName;
    }

    public String getBuilderName() {
        return builderName;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
        locationConfig.saveLocation("Maps." + mapName + ".Spawn", location);
        protectionLocation = new Location(spawnLocation.getWorld(), spawnLocation.getBlockX(), spawnLocation.getBlockY(), spawnLocation.getBlockZ());
    }

    public Location getTopLocation() {
        return topLocation;
    }

    public void setTopLocation(Location location) {
        this.topLocation = location;
        locationConfig.saveLocation("Maps." + mapName + ".Top", location);
    }

    public ItemStack[] getItems() {
        return items;
    }

    public void setItems(ItemStack[] newItems) {
        this.items = getNewItems(newItems);
        locationConfig.saveKitItems(mapName, base64Serializer.itemStackArrayToBase64(items));
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public void setArmor(ItemStack[] newArmor) {
        this.armor = getNewItems(newArmor);
        locationConfig.saveKitArmor(mapName, base64Serializer.itemStackArrayToBase64(armor));
    }

    private ItemStack[] getNewItems(ItemStack[] changeItems) {
        for (ItemStack item : changeItems) {
            if (item == null) {
                continue;
            }
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta == null) {
                continue;
            }
            itemMeta.setUnbreakable(true);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(itemMeta);
        }
        return changeItems;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        locationConfig.saveRadius(mapName, radius);
    }

    public boolean isCuboid() {
        return isCuboid;
    }

    public void setCuboid(boolean cuboid) {
        isCuboid = cuboid;
        locationConfig.saveCuboid(mapName, cuboid);
    }
}
