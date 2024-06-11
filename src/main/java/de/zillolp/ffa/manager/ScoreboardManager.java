package de.zillolp.ffa.manager;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.LanguageConfig;
import de.zillolp.ffa.config.customconfigs.PluginConfig;
import de.zillolp.ffa.map.Map;
import de.zillolp.ffa.map.MapManager;
import de.zillolp.ffa.utils.ReflectionUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ScoreboardManager {
    private final ReflectionUtil reflectionUtil;
    private final PluginConfig pluginConfig;
    private final LanguageConfig languageConfig;
    private final PlayerManager playerManager;
    private final MapManager mapManager;
    private final HashMap<UUID, Scoreboard> playerSideBoards;
    private final HashMap<UUID, Scoreboard> playerHealthBoards;
    private final HashMap<UUID, Integer> playerHealth;


    public ScoreboardManager(FFA plugin) {
        this.reflectionUtil = plugin.getReflectionUtil();
        this.pluginConfig = plugin.getPluginConfig();
        this.languageConfig = plugin.getLanguageConfig();
        this.playerManager = plugin.getPlayerManager();
        this.mapManager = plugin.getMapManager();
        this.playerSideBoards = new HashMap<>();
        this.playerHealthBoards = new HashMap<>();
        this.playerHealth = new HashMap<>();
    }

    public void createScoreboards(Player player) {
        UUID uuid = player.getUniqueId();
        HashMap<UUID, Map> playerMaps = mapManager.getPlayerMaps();
        if (!(playerMaps.containsKey(uuid))) {
            return;
        }
        FileConfiguration fileConfiguration = pluginConfig.getFileConfiguration();
        if (fileConfiguration.getBoolean("Scoreboard", true)) {
            createSideBoard(player);
        }
        if (!(fileConfiguration.getBoolean("Hearts", true))) {
            return;
        }
        createHealthBoard(player);
        World world = Bukkit.getWorld(playerMaps.get(uuid).getMapName());
        if (world == null) {
            return;
        }
        for (Player player1 : world.getPlayers()) {
            sendHealth(player1);
        }
    }

    public void createSideBoard(Player player) {
        Scoreboard scoreboard = new Scoreboard();
        playerSideBoards.put(player.getUniqueId(), scoreboard);
        Objective sidebarObjective = new Objective(scoreboard, "sideBoard", ObjectiveCriteria.DUMMY, Component.literal(languageConfig.getTranslatedLanguage("TITLE")), ObjectiveCriteria.DUMMY.getDefaultRenderType(), false, null);
        createObjective(player, sidebarObjective, DisplaySlot.SIDEBAR);

        String[] lines = languageConfig.getScoreboardLines("LINES", playerManager.getPlayerProfiles().get(player.getUniqueId()));
        int scoreCount = lines.length;
        for (String line : lines) {
            createTeam(player.getUniqueId(), "line" + scoreCount, line);
            reflectionUtil.sendPacket(new ClientboundSetScorePacket("line" + scoreCount, "sideBoard", scoreCount, Optional.of(Component.literal(line)), Optional.empty()), player);
            scoreCount--;
        }
    }

    public void createHealthBoard(Player player) {
        UUID uuid = player.getUniqueId();
        Scoreboard scoreboard = new Scoreboard();
        playerHealthBoards.put(uuid, scoreboard);
        playerHealth.put(uuid, (int) (player.getHealth() / 2));
        Objective belowNameObjective = new Objective(scoreboard, "belowName", ObjectiveCriteria.DUMMY, Component.literal(languageConfig.getTranslatedLanguage("HEARTS")), ObjectiveCriteria.DUMMY.getDefaultRenderType(), false, null);
        createObjective(player, belowNameObjective, DisplaySlot.BELOW_NAME);
        sendHealth(player);
    }

    public void updateSideBoard(Player player) {
        UUID uuid = player.getUniqueId();
        Scoreboard scoreboard = playerSideBoards.get(uuid);
        String[] lines = languageConfig.getScoreboardLines("LINES", playerManager.getPlayerProfiles().get(player.getUniqueId()));
        int scoreCount = lines.length;
        for (String prefix : lines) {
            String line = "line" + scoreCount;
            PlayerTeam playerTeam = scoreboard.getPlayerTeam(line);
            if (playerTeam == null) {
                createTeam(uuid, line, prefix);
                scoreCount--;
                continue;
            }
            String currentPrefix = playerTeam.getPlayerPrefix().getString();
            if (currentPrefix.equalsIgnoreCase(prefix)) {
                scoreCount--;
                continue;
            }
            playerTeam.setPlayerPrefix(Component.literal(prefix));
            reflectionUtil.sendPacket(new ClientboundSetScorePacket(line, "sideBoard", scoreCount, Optional.of(Component.literal(prefix)), Optional.empty()), player);
            scoreCount--;
        }
    }

    public void updateHealthBoard(Player player) {
        UUID uuid = player.getUniqueId();
        int health = (int) (player.getHealth() / 2);
        if (!(playerHealth.containsKey(uuid))) {
            playerHealth.put(uuid, health);
            sendHealth(player);
            return;
        }
        if (playerHealth.get(uuid) == health) {
            return;
        }
        playerHealth.replace(uuid, health);
        sendHealth(player);
    }

    public void delete(Player player) {
        UUID uuid = player.getUniqueId();
        if (playerSideBoards.containsKey(uuid)) {
            reflectionUtil.sendPacket(new ClientboundSetDisplayObjectivePacket(DisplaySlot.SIDEBAR, null), player);
            playerSideBoards.remove(uuid);
        }
        if (!(playerHealthBoards.containsKey(uuid) && playerHealth.containsKey(uuid))) {
            return;
        }
        reflectionUtil.sendPacket(new ClientboundSetDisplayObjectivePacket(DisplaySlot.BELOW_NAME, null), player);
        playerHealthBoards.remove(uuid);
        playerHealth.remove(uuid);
    }

    private void createObjective(Player player, Objective objective, DisplaySlot displaySlot) {
        reflectionUtil.sendPacket(new ClientboundSetObjectivePacket(objective, 1), player);
        reflectionUtil.sendPacket(new ClientboundSetObjectivePacket(objective, 0), player);
        reflectionUtil.sendPacket(new ClientboundSetDisplayObjectivePacket(displaySlot, objective), player);
    }

    private void createTeam(UUID uuid, String name, String prefix) {
        PlayerTeam playerTeam = playerSideBoards.get(uuid).addPlayerTeam(name);
        playerTeam.setPlayerPrefix(Component.literal(prefix));
    }

    public void sendHealth(Player player) {
        World world = Bukkit.getWorld(mapManager.getPlayerMaps().get(player.getUniqueId()).getMapName());
        if (world == null) {
            return;
        }
        for (Player player1 : world.getPlayers()) {
            reflectionUtil.sendPacket(new ClientboundSetScorePacket(player.getName(), "belowName", (int) (player.getHealth() / 2), Optional.empty(), Optional.empty()), player1);
        }
    }

    public boolean hasScoreboard(UUID uuid) {
        return playerSideBoards.containsKey(uuid);
    }

    public boolean hasHealth(UUID uuid) {
        return playerHealthBoards.containsKey(uuid) && playerHealth.containsKey(uuid);
    }
}
