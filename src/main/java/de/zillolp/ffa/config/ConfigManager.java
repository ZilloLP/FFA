package de.zillolp.ffa.config;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.config.customconfigs.*;

public class ConfigManager {
    private final FFA plugin;
    private PluginConfig pluginConfig;
    private LanguageConfig languageConfig;
    private LocationConfig locationConfig;
    private PermissionsConfig permissionsConfig;
    private MySQLConfig mySQLConfig;

    public ConfigManager(FFA plugin) {
        this.plugin = plugin;
        this.registerConfigs();
    }

    private void registerConfigs() {
        this.pluginConfig = new PluginConfig(plugin, "config.yml");
        this.languageConfig = new LanguageConfig(plugin, "language.yml");
        this.locationConfig = new LocationConfig(plugin, "locations.yml");
        this.permissionsConfig = new PermissionsConfig(plugin, "permissions.yml");
        this.mySQLConfig = new MySQLConfig(plugin, "mysql.yml");
    }

    public void reloadConfigs() {
        pluginConfig.loadConfiguration();
        languageConfig.loadConfiguration();
        locationConfig.loadConfiguration();
        permissionsConfig.loadConfiguration();
        mySQLConfig.loadConfiguration();
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

    public MySQLConfig getMySQLConfig() {
        return mySQLConfig;
    }
}
