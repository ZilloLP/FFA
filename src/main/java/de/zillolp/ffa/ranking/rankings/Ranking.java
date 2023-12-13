package de.zillolp.ffa.ranking.rankings;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.manager.PlayerManager;

import java.util.LinkedHashMap;

public abstract class Ranking implements Runnable {
    protected final FFA plugin;
    protected final PlayerManager playerManager;
    protected final LinkedHashMap<String, Long> cachedData;
    protected final LinkedHashMap<String, Long> sortedData;
    private int taskID;
    private boolean isRunning;

    public Ranking(FFA plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.cachedData = new LinkedHashMap<>();
        this.sortedData = new LinkedHashMap<>();
    }

    @Override
    public void run() {
        rankData();
        setRanking();
    }

    public abstract void rankData();

    public abstract void setRanking();

    public void start() {
        isRunning = true;
        taskID = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, this, 0, plugin.getPluginConfig().getFileConfiguration().getInt("STATS_REFRESH_SECONDS", 10) * 20L);
    }

    public void stop() {
        isRunning = false;
        plugin.getServer().getScheduler().cancelTask(taskID);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public LinkedHashMap<String, Long> getSortedData() {
        return sortedData;
    }
}
