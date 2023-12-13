package de.zillolp.ffa.config.customconfigs;

import de.zillolp.ffa.FFA;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomConfig {
    protected final FFA plugin;
    private final File file;
    protected FileConfiguration fileConfiguration;

    public CustomConfig(FFA plugin, String name) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), name);
        if (!(file.exists())) {
            plugin.saveResource(name, true);
        }
        loadConfiguration();
    }

    public void loadConfiguration() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    protected void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }
}
