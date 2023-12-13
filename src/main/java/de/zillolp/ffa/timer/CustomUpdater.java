package de.zillolp.ffa.timer;

import de.zillolp.ffa.FFA;

public abstract class CustomUpdater implements Runnable {
    protected final FFA plugin;
    private final int taskID;

    public CustomUpdater(FFA plugin, int ticks) {
        this.plugin = plugin;
        this.taskID = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, this, 0, ticks);
    }

    @Override
    public void run() {
        tick();
    }

    protected abstract void tick();

    public void stop() {
        plugin.getServer().getScheduler().cancelTask(taskID);
    }
}
