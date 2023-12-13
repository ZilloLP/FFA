package de.zillolp.ffa.hologram;

import de.zillolp.ffa.hologram.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HologramManager {
    private final HashMap<UUID, HashMap<Hologram, Location>> hologramLists;

    public HologramManager() {
        this.hologramLists = new HashMap<>();
    }

    public void spawnHologram(Player player, Hologram hologram, Location location) {
        UUID uuid = player.getUniqueId();
        HashMap<Hologram, Location> holograms;
        if (hologramLists.containsKey(uuid)) {
            holograms = hologramLists.get(uuid);
        } else {
            holograms = new HashMap<>();
            hologramLists.put(uuid, holograms);
        }
        if (location.getWorld() != player.getWorld()) {
            return;
        }
        holograms.put(hologram, location);
        hologram.spawn(player, location);
    }

    public void deleteHologram(Player player, Location location) {
        UUID uuid = player.getUniqueId();
        if (!(hologramLists.containsKey(uuid))) {
            return;
        }
        Hologram hologram = getHologram(player, location);
        if (hologram == null) {
            return;
        }
        hologram.destroy(player);
        hologramLists.get(uuid).remove(hologram);
    }

    public void deleteHolograms(Player player) {
        UUID uuid = player.getUniqueId();
        if (!(hologramLists.containsKey(uuid))) {
            return;
        }
        HashMap<Hologram, Location> hologramList = hologramLists.get(uuid);
        for (Map.Entry<Hologram, Location> hologramEntry : hologramList.entrySet()) {
            hologramEntry.getKey().destroy(player);
        }
        hologramLists.remove(uuid);
    }

    public Hologram getHologram(Player player, Location location) {
        HashMap<Hologram, Location> hologramList = hologramLists.get(player.getUniqueId());
        for (Map.Entry<Hologram, Location> hologramEntry : hologramList.entrySet()) {
            Location spawnLocation = hologramEntry.getValue();
            if (spawnLocation == null || spawnLocation.getX() != location.getX() || spawnLocation.getY() != location.getY() || spawnLocation.getZ() != location.getZ()) {
                continue;
            }
            return hologramEntry.getKey();
        }
        return null;
    }

    public HashMap<UUID, HashMap<Hologram, Location>> getHologramLists() {
        return hologramLists;
    }
}
