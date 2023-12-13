package de.zillolp.ffa.config.customconfigs;

import de.zillolp.ffa.FFA;
import org.bukkit.entity.Player;

public class PermissionsConfig extends CustomConfig {
    public PermissionsConfig(FFA plugin, String name) {
        super(plugin, name);
    }

    public boolean hasPermission(Player player, String key) {
        return player.hasPermission(fileConfiguration.getString(key, key));
    }
}
