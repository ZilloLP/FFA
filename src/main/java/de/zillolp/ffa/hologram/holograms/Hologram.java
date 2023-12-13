package de.zillolp.ffa.hologram.holograms;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.utils.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Hologram {
    protected final FFA plugin;
    protected final ReflectionUtil reflectionUtil;

    public Hologram(FFA plugin) {
        this.plugin = plugin;
        this.reflectionUtil = plugin.getReflectionUtil();
    }

    public abstract void spawn(Player player, Location location);

    public abstract void destroy(Player player);
}
