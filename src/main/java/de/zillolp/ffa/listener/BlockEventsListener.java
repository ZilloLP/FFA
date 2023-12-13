package de.zillolp.ffa.listener;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.map.Map;
import de.zillolp.ffa.profiles.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class BlockEventsListener implements Listener {
    private final FFA plugin;
    private final HashMap<UUID, PlayerProfile> playerProfiles;
    private final HashMap<UUID, Map> playerMaps;
    private final Material[] blacklistedTypes;

    public BlockEventsListener(FFA plugin) {
        this.plugin = plugin;
        this.playerProfiles = plugin.getPlayerManager().getPlayerProfiles();
        this.playerMaps = plugin.getMapManager().getPlayerMaps();
        this.blacklistedTypes = new Material[]{Material.FARMLAND, Material.ENCHANTING_TABLE, Material.ENDER_CHEST, Material.CHEST, Material.TRAPPED_CHEST, Material.ANVIL, Material.TNT, Material.DROPPER, Material.HOPPER, Material.DISPENSER};
    }

    @EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (player.getWorld().getName().equalsIgnoreCase(map.getMapName())) {
            return;
        }
        if (!(playerProfiles.containsKey(uuid))) {
            return;
        }
        PlayerProfile playerProfile = playerProfiles.get(uuid);
        playerProfile.setLastLocation(null);
        playerProfile.resetPlayer(player);
        playerMaps.remove(uuid);
        plugin.getScoreboardManager().delete(player);
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player) event.getEntity();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        if (!(map.isLocationInProtection(player.getLocation()) || map.isLocationInProtection(event.getDamager().getLocation()))) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player) event.getEntity();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        if (!(map.isLocationInProtection(player.getLocation()))) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player) event.getEntity();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player) event.getEntity();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        if (!(map.isLocationInProtection(player.getLocation()))) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        if (!(map.isLocationInProtection(player.getLocation()))) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null || (!(Arrays.asList(blacklistedTypes).contains(block.getType())))) {
            return;
        }
        event.setCancelled(true);
    }
}
