package de.zillolp.ffa.manager;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BlockManager implements Runnable {
    private final PluginConfig pluginConfig;
    private final HashMap<Block, Integer> placedBlocks;
    private final Material[] instantReplaceTypes;

    public BlockManager(FFA plugin) {
        this.pluginConfig = plugin.getPluginConfig();
        this.placedBlocks = new HashMap<>();
        this.instantReplaceTypes = new Material[]{Material.AIR, Material.LADDER, Material.WATER, Material.LAVA, Material.POWDER_SNOW};
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 1);
    }

    @Override
    public void run() {
        ArrayList<Block> removeBlocks = new ArrayList<>();
        for (java.util.Map.Entry<Block, Integer> blockEntry : placedBlocks.entrySet()) {
            Block block = blockEntry.getKey();
            int seconds = blockEntry.getValue();
            seconds--;
            placedBlocks.replace(block, seconds);
            if (seconds == 80) {
                if (Arrays.asList(instantReplaceTypes).contains(block.getType())) {
                    block.setType(Material.AIR);
                    removeBlocks.add(block);
                    continue;
                }
                block.setType(Material.valueOf(pluginConfig.getFileConfiguration().getString("Block replace type", "EMERALD_BLOCK")));
                continue;
            }
            if (seconds > 0) {
                continue;
            }
            block.setType(Material.AIR);
            removeBlocks.add(block);
        }
        for (Block block : removeBlocks) {
            placedBlocks.remove(block);
        }
    }

    public void resetBlocks() {
        for (Block block : placedBlocks.keySet()) {
            block.setType(Material.AIR);
        }
    }

    public HashMap<Block, Integer> getPlacedBlocks() {
        return placedBlocks;
    }
}
