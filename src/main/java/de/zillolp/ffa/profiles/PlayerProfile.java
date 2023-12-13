package de.zillolp.ffa.profiles;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.database.DatabaseManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class PlayerProfile {
    private final FFA plugin;
    private final UUID uuid;
    private String name;
    private long kills;
    private long deaths;
    private long killStreak;
    private ItemStack[] items;
    private ItemStack[] armor;
    private Location lastLocation;

    public PlayerProfile(FFA plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        plugin.getDatabaseManager().loadProfile(this);
    }

    public PlayerProfile(FFA plugin, Player player) {
        this.plugin = plugin;
        this.uuid = player.getUniqueId();
        DatabaseManager databaseManager = plugin.getDatabaseManager();
        this.name = player.getName();
        if (!(databaseManager.playerExists(uuid, name))) {
            databaseManager.createPlayer(uuid, name);
            kills = 0;
            deaths = 0;
            return;
        }
        databaseManager.loadProfile(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getKills() {
        return kills;
    }

    public void setKills(long kills) {
        this.kills = kills;
    }

    public void addKills(long kills) {
        setKills(getKills() + kills);
    }

    public long getDeaths() {
        return deaths;
    }

    public void setDeaths(long deaths) {
        this.deaths = deaths;
    }

    public void addDeaths(long deaths) {
        setDeaths(getDeaths() + deaths);
    }

    public long getKillStreak() {
        return killStreak;
    }

    public void setKillStreak(long killStreak) {
        this.killStreak = killStreak;
    }

    public void addKillStreak(long kills) {
        setKillStreak(getKillStreak() + kills);
    }

    public ItemStack[] getItems() {
        return items;
    }

    public void setItems(ItemStack[] items) {
        this.items = items;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public void setArmor(ItemStack[] armor) {
        this.armor = armor;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public void resetPlayer(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        if (items != null) {
            playerInventory.setContents(items);
            items = null;
        }
        if (armor != null) {
            playerInventory.setArmorContents(armor);
            armor = null;
        }
        if (lastLocation != null) {
            player.teleport(lastLocation);
            lastLocation = null;
        }
        killStreak = 0;
    }

    public void uploadData(Player player) {
        resetPlayer(player);
        plugin.getDatabaseManager().uploadProfile(this);
    }
}
