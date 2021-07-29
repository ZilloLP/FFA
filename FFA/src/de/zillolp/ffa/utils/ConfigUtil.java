package de.zillolp.ffa.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;

import de.zillolp.ffa.main.Main;

public class ConfigUtil {
	private YamlConfiguration config;
	private File Config;
	private FileConfigurationOptions options;

	public ConfigUtil(File Config) {
		this.config = YamlConfiguration.loadConfiguration(Config);
		this.Config = Config;
		this.options = config.options();
	}

	public ConfigUtil(YamlConfiguration config, File Config) {
		this.config = config;
		this.Config = Config;
		this.options = config.options();
	}

	public ConfigUtil(YamlConfiguration config, File Config, FileConfigurationOptions options) {
		this.config = config;
		this.Config = Config;
		this.options = options;
	}

	public boolean isConfig() {
		return Config.exists();
	}

	public void createConfig() {
		createHeader();
		options.copyDefaults(true);
		saveFile();
	}

	public void createHeader() {
		options.header("#########################################\n" + "################# [FFA] #################\n"
				+ "#########################################\n\n");
	}

	public void addComment(String comment) {
		config.addDefault(Main.plugin.getName() + "_COMMENT_" + getCommentsNum(Config), " " + comment);
		options.copyDefaults(true);
		save();
	}

	public void addDefaultStrings(LinkedHashMap<String, String> defaults) {
		for (Entry<String, String> section : defaults.entrySet())
			config.addDefault(section.getKey(), section.getValue());
		options.copyDefaults(true);
		save();
	}

	public void addDefaultString(String section, String value) {
		config.addDefault(section, value);
		options.copyDefaults(true);
		save();
	}

	public void addDefaultBooleans(LinkedHashMap<String, Boolean> defaults) {
		for (Entry<String, Boolean> section : defaults.entrySet())
			config.addDefault(section.getKey(), section.getValue());
		options.copyDefaults(true);
		save();
	}

	public void addDefaultBoolean(String section, boolean value) {
		config.addDefault(section, value);
		options.copyDefaults(true);
		save();
	}

	public void addDefaultIntegers(LinkedHashMap<String, Integer> defaults) {
		for (Entry<String, Integer> section : defaults.entrySet())
			config.addDefault(section.getKey(), section.getValue());
		options.copyDefaults(true);
		save();
	}

	public void addDefaultInteger(String section, int value) {
		config.addDefault(section, value);
		options.copyDefaults(true);
		save();
	}

	public void setString(String section, String value) {
		config.set(section, value);
		save();
	}

	public void setBoolean(String section, Boolean value) {
		config.set(section, value);
		save();
	}

	public void setInteger(String section, int value) {
		config.set(section, value);
		save();
	}

	public void setDouble(String section, double value) {
		config.set(section, value);
		save();
	}

	public YamlConfiguration getConfig() {
		return config;
	}

	public ConfigurationSection getConfigsection(String section) {
		return config.getConfigurationSection(section);
	}

	public String getString(String section) {
		return config.getString(section);
	}

	public boolean getBoolean(String section) {
		return config.getBoolean(section);
	}

	public int getInt(String section) {
		return config.getInt(section);
	}

	public double getDouble(String section) {
		return config.getDouble(section);
	}

	public void save() {
		String configuration = prepareConfigString(config.saveToString());
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(Config));
			writer.write(configuration);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveFile() {
		try {
			config.save(Config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int getCommentsNum(File file) {
		try {
			int comments = 0;
			String currentLine;
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.startsWith("#")) {
					comments++;
				}
			}
			reader.close();
			return comments;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	private String prepareConfigString(String configString) {
		int lastLine = 0;
		int headerLine = 0;
		String[] lines = configString.split("\n");
		StringBuilder config = new StringBuilder("");
		for (String line : lines) {
			if (line.startsWith(Main.plugin.getName() + "_COMMENT")) {
				String comment = "#" + line.trim().substring(line.indexOf(":") + 1);
				if (comment.startsWith("# ##")) {
					/*
					 * If header line = 0 then it is header start, if it's equal to 1 it's the end
					 * of header
					 */
					if (headerLine == 0) {
						config.append(comment + "\n");
						lastLine = 0;
						headerLine = 1;
					} else if (headerLine == 1) {
						config.append(comment + "\n\n");
						lastLine = 0;
						headerLine = 0;
					}
				} else {
					/*
					 * Last line = 0 - Comment Last line = 1 - Normal path
					 */
					String normalComment;
					if (comment.startsWith("# ' ")) {
						normalComment = comment.substring(0, comment.length() - 1).replaceFirst("# ' ", "# ");
					} else {
						normalComment = comment;
					}
					if (lastLine == 0) {
						config.append(normalComment + "\n");
					} else if (lastLine == 1) {
						config.append("\n" + normalComment + "\n");
					}
					lastLine = 0;
				}
			} else {
				config.append(line + "\n");
				lastLine = 1;
			}
		}
		return config.toString();
	}
}
