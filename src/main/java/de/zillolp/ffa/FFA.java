package de.zillolp.ffa;

import de.zillolp.ffa.bstats.Metrics;
import de.zillolp.ffa.commands.maincommands.FFACommand;
import de.zillolp.ffa.commands.subcommands.*;
import de.zillolp.ffa.config.ConfigManager;
import de.zillolp.ffa.config.customconfigs.*;
import de.zillolp.ffa.database.DatabaseConnector;
import de.zillolp.ffa.database.DatabaseManager;
import de.zillolp.ffa.hologram.HologramManager;
import de.zillolp.ffa.listener.BlockEventsListener;
import de.zillolp.ffa.listener.BlockPlaceListener;
import de.zillolp.ffa.listener.PlayerConnectionListener;
import de.zillolp.ffa.listener.RespawnListener;
import de.zillolp.ffa.manager.BlockManager;
import de.zillolp.ffa.manager.PlayerManager;
import de.zillolp.ffa.manager.ScoreboardManager;
import de.zillolp.ffa.map.MapManager;
import de.zillolp.ffa.placeholder.PlaceholderListener;
import de.zillolp.ffa.ranking.RankingManager;
import de.zillolp.ffa.serializer.Base64Serializer;
import de.zillolp.ffa.timer.MapUpdater;
import de.zillolp.ffa.timer.ScoreboardUpdater;
import de.zillolp.ffa.utils.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.logging.Level;

public final class FFA extends JavaPlugin {
    private PluginConfig pluginConfig;
    private LanguageConfig languageConfig;
    private LocationConfig locationConfig;
    private PermissionsConfig permissionsConfig;
    private MySQLConfig mySQLConfig;
    private DatabaseConnector databaseConnector;
    private Base64Serializer base64Serializer;
    private ReflectionUtil reflectionUtil;
    private ConfigManager configManager;
    private MapManager mapManager;
    private DatabaseManager databaseManager;
    private HologramManager hologramManager;
    private PlayerManager playerManager;
    private BlockManager blockManager;
    private RankingManager rankingManager;
    private ScoreboardManager scoreboardManager;
    private MapUpdater mapUpdater;
    private SetSubCommand setSubCommand;
    private JoinSubCommand joinSubCommand;

    @Override
    public void onEnable() {
        this.registerConfigs();
        this.connectDatabase();
        if (!(this.databaseConnector.hasConnection())) {
            this.getCommand("ffa").setExecutor(new FFACommand(this));
            this.getLogger().log(Level.WARNING, "Could not connect to database!");
            this.databaseConnector.close();
            return;
        }
        this.base64Serializer = new Base64Serializer();
        this.reflectionUtil = new ReflectionUtil();
        this.registerManager();
        this.registerUpdater();
        this.registerCommands();
        this.registerListener(Bukkit.getPluginManager());
        this.loadPlayers();
        this.registerMetrics();
    }

    @Override
    public void onDisable() {
        if (!(this.databaseConnector.hasConnection())) {
            return;
        }
        this.blockManager.resetBlocks();
        this.unloadPlayers();
        this.databaseConnector.close();
    }

    private void connectDatabase() {
        boolean useMySQL = pluginConfig.getFileConfiguration().getBoolean("MySQL", false);
        FileConfiguration mysqlConfiguration = mySQLConfig.getFileConfiguration();
        String address = mysqlConfiguration.getString("Host", "N/A");
        String port = mysqlConfiguration.getString("Port", "N/A");
        String databaseName = mysqlConfiguration.getString("Database", "N/A");
        String username = mysqlConfiguration.getString("User", "N/A");
        String password = mysqlConfiguration.getString("Password", "N/A");
        this.databaseConnector = new DatabaseConnector(this, useMySQL, "ffa", address, port, databaseName, username, password);
    }

    private void registerConfigs() {
        this.configManager = new ConfigManager(this);
        this.pluginConfig = configManager.getPluginConfig();
        this.languageConfig = configManager.getLanguageConfig();
        this.locationConfig = configManager.getLocationConfig();
        this.permissionsConfig = configManager.getPermissionsConfig();
        this.mySQLConfig = configManager.getMySQLConfig();
    }

    private void registerManager() {
        this.mapManager = new MapManager(this);
        this.databaseManager = new DatabaseManager(this);
        this.hologramManager = new HologramManager();
        this.playerManager = new PlayerManager(this);
        this.rankingManager = new RankingManager(this);
        this.blockManager = new BlockManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
    }

    private void registerUpdater() {
        this.mapUpdater = new MapUpdater(this);
        new ScoreboardUpdater(this);
    }

    private void registerCommands() {
        FFACommand ffaCommand = new FFACommand(this);
        this.getCommand("ffa").setExecutor(ffaCommand);
        ffaCommand.registerSubCommand(new CreateSubCommand(this, "create"));
        ffaCommand.registerSubCommand(new HelpSubCommand(this, "help"));
        ffaCommand.registerSubCommand(new ReloadSubCommand(this, "reload"));
        ffaCommand.registerSubCommand(new LeaveSubCommand(this, "leave"));
        this.setSubCommand = new SetSubCommand(this, "set");
        setSubCommand.setSubCommands(getMapSubCommands(setSubCommand));
        ffaCommand.registerSubCommand(setSubCommand);
        this.joinSubCommand = new JoinSubCommand(this, "join");
        joinSubCommand.setSubCommands(getMapSubCommands(joinSubCommand));
        ffaCommand.registerSubCommand(joinSubCommand);
    }

    public String[] getMapSubCommands(SubCommand subCommand) {
        StringBuilder maps = new StringBuilder();
        StringBuilder playAbleMaps = new StringBuilder();
        for (Map.Entry<String, de.zillolp.ffa.map.Map> mapEntry : mapManager.getMaps().entrySet()) {
            String mapName = mapEntry.getKey();
            maps.append(mapName).append(";");
            if (!(mapEntry.getValue().isPlayable())) {
                continue;
            }
            playAbleMaps.append(mapName).append(";");
        }
        if (subCommand instanceof SetSubCommand) {
            return new String[]{maps.toString(), "spawn;kit;top;protection;radius"};
        } else if (subCommand instanceof JoinSubCommand) {
            return new String[]{playAbleMaps.toString()};
        }
        return new String[0];
    }

    private void registerListener(PluginManager pluginManager) {
        pluginManager.registerEvents(new BlockEventsListener(this), this);
        pluginManager.registerEvents(new BlockPlaceListener(this), this);
        pluginManager.registerEvents(new PlayerConnectionListener(this), this);
        pluginManager.registerEvents(new RespawnListener(this), this);
        if (pluginManager.getPlugin("PlaceholderAPI") != null) {
            new PlaceholderListener(this);
        }
    }

    private void registerMetrics() {
        new Metrics(this, 12256);
    }


    public void loadPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerManager.registerPlayer(player, false);
        }
        databaseManager.loadProfiles();
    }

    public void unloadPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerManager.unregisterPlayer(player, false);
        }
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    public LanguageConfig getLanguageConfig() {
        return languageConfig;
    }

    public LocationConfig getLocationConfig() {
        return locationConfig;
    }

    public PermissionsConfig getPermissionsConfig() {
        return permissionsConfig;
    }

    public DatabaseConnector getDatabaseConnector() {
        return databaseConnector;
    }

    public Base64Serializer getBase64Serializer() {
        return base64Serializer;
    }

    public ReflectionUtil getReflectionUtil() {
        return reflectionUtil;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public RankingManager getRankingManager() {
        return rankingManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public MapUpdater getMapUpdater() {
        return mapUpdater;
    }

    public SetSubCommand getSetSubCommand() {
        return setSubCommand;
    }

    public JoinSubCommand getJoinSubCommand() {
        return joinSubCommand;
    }
}
