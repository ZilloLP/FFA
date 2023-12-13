package de.zillolp.ffa.manager;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.profiles.PlayerProfile;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    private final FFA plugin;
    private final HashMap<UUID, PlayerProfile> playerProfiles;

    public PlayerManager(FFA plugin) {
        this.plugin = plugin;
        this.playerProfiles = new HashMap<>();
    }

    public void registerPlayer(Player player, boolean isAsync) {
        if (!(isAsync)) {
            registerPlayer(player);
            return;
        }
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                registerPlayer(player);
            }
        }, 1);
    }

    private void registerPlayer(Player player) {
        playerProfiles.put(player.getUniqueId(), new PlayerProfile(plugin, player));
    }

    public void unregisterPlayer(Player player, boolean isAsync) {
        if (!(isAsync)) {
            unregisterPlayer(player);
            return;
        }
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                unregisterPlayer(player);
            }
        });
    }

    private void unregisterPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        plugin.getHologramManager().deleteHolograms(player);
        plugin.getMapManager().getPlayerMaps().remove(uuid);
        plugin.getScoreboardManager().delete(player);
        if (!(playerProfiles.containsKey(uuid))) {
            return;
        }
        playerProfiles.get(uuid).uploadData(player);
    }

    public HashMap<UUID, PlayerProfile> getPlayerProfiles() {
        return playerProfiles;
    }
}
