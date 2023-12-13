package de.zillolp.ffa.listener;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.manager.BlockManager;
import de.zillolp.ffa.map.Map;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.HashMap;
import java.util.UUID;

public class BlockPlaceListener implements Listener {
    private final BlockManager blockManager;
    private final HashMap<UUID, Map> playerMaps;

    public BlockPlaceListener(FFA plugin) {
        this.blockManager = plugin.getBlockManager();
        this.playerMaps = plugin.getMapManager().getPlayerMaps();
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        Block block = event.getBlock();
        if (map.isLocationInProtection(block.getLocation())) {
            event.setCancelled(true);
            return;
        }
        blockManager.getPlacedBlocks().put(block, 140);
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        Block block = event.getBlock();
        if (map.isLocationInProtection(block.getLocation())) {
            event.setCancelled(true);
            return;
        }
        blockManager.getPlacedBlocks().put(block, 140);
    }


    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        Block block = event.getBlock();
        HashMap<Block, Integer> placedBlocks = blockManager.getPlacedBlocks();
        if (placedBlocks.containsKey(event.getBlock())) {
            event.setDropItems(false);
            event.setExpToDrop(0);
            placedBlocks.remove(block);
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFadeEvent(BlockFadeEvent event) {
        Block block = event.getBlock();
        if (!(blockManager.getPlacedBlocks().containsKey(block))) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurnEvent(BlockBurnEvent event) {
        Block block = event.getBlock();
        if (!(blockManager.getPlacedBlocks().containsKey(block))) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPhysicsEvent(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (!(blockManager.getPlacedBlocks().containsKey(block))) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent event) {
        Block block = event.getBlock();
        if (!(blockManager.getPlacedBlocks().containsKey(block))) {
            return;
        }
        event.setCancelled(true);
    }
}
