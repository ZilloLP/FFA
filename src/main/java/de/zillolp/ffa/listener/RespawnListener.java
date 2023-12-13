package de.zillolp.ffa.listener;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.LanguageConfig;
import de.zillolp.ffa.map.Map;
import de.zillolp.ffa.profiles.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.PlayerInventory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RespawnListener implements Listener {
    private final FFA plugin;
    private final LanguageConfig languageConfig;
    private final ArrayList<UUID> deadPlayers;
    private final HashMap<UUID, Map> playerMaps;

    public RespawnListener(FFA plugin) {
        this.plugin = plugin;
        this.languageConfig = plugin.getLanguageConfig();
        this.deadPlayers = new ArrayList<>();
        this.playerMaps = plugin.getMapManager().getPlayerMaps();
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid) || deadPlayers.contains(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        event.setRespawnLocation(map.getSpawnLocation());
        deadPlayers.remove(uuid);
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();
        if (!(playerMaps.containsKey(uuid) || deadPlayers.contains(uuid))) {
            return;
        }
        Map map = playerMaps.get(uuid);
        if (!(player.getWorld().getName().equalsIgnoreCase(map.getMapName()))) {
            return;
        }
        event.setDeathMessage(null);
        event.setKeepInventory(true);
        event.setKeepLevel(true);
        event.setDroppedExp(0);
        event.getDrops().clear();
        respawnPlayer(player);
        HashMap<UUID, PlayerProfile> playerProfiles = plugin.getPlayerManager().getPlayerProfiles();
        PlayerProfile playerProfile = playerProfiles.get(uuid);
        playerProfile.addDeaths(1);
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setContents(map.getItems());
        playerInventory.setArmorContents(map.getArmor());
        if (player.getKiller() == null) {
            long killStreak = playerProfile.getKillStreak();
            player.sendMessage(languageConfig.getLanguageWithPrefix("DEATH_MESSAGE"));
            if (killStreak < 3) {
                return;
            }
            player.sendMessage(languageConfig.getLanguageWithPrefix("KILL_STREAK_LOSE").replace("%killstreak%", languageConfig.formatNumber(killStreak)));
        } else {
            Player killer = player.getKiller();
            double hearts = killer.getHealth() / 2;
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            String health = numberFormat.format(hearts);
            killer.sendMessage(languageConfig.getLanguageWithPrefix("KILL_MESSAGE").replace("%player%", player.getName()).replace("%health%", health));
            player.sendMessage(languageConfig.getLanguageWithPrefix("KILLED_MESSAGE").replace("%player%", killer.getName()).replace("%health%", health));
            PlayerProfile killerProfile = playerProfiles.get(killer.getUniqueId());
            killerProfile.addKills(1);
            killerProfile.addKillStreak(1);
            long killStreak = killerProfile.getKillStreak();
            if (killStreak == 3 || killStreak % 5 == 0) {
                String message = languageConfig.getLanguageWithPrefix("KILL_STREAK_MESSAGE").replace("%player%", killer.getName()).replace("%killstreak%", languageConfig.formatNumber(killStreak));
                for (Player player1 : player.getWorld().getPlayers()) {
                    if (!(plugin.getMapManager().getPlayerMaps().containsKey(player1.getUniqueId()))) {
                        continue;
                    }
                    player1.sendMessage(message);
                }
            }
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    killer.setHealth(20);
                }
            }, 1);
        }
        playerProfile.setKillStreak(0);
    }

    private void respawnPlayer(Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                player.spigot().respawn();
            }
        }, 1);
    }

}
