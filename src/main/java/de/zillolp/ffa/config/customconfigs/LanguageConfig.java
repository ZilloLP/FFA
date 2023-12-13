package de.zillolp.ffa.config.customconfigs;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.map.Map;
import de.zillolp.ffa.profiles.PlayerProfile;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class LanguageConfig extends CustomConfig {
    public LanguageConfig(FFA plugin, String name) {
        super(plugin, name);
    }

    public String getTranslatedLanguage(String key) {
        if (!(fileConfiguration.contains(key))) {
            return key;
        }
        String language = fileConfiguration.getString(key, key + " ");
        language = ChatColor.translateAlternateColorCodes('&', language);
        language = language.replace("%Ae%", "Ä");
        language = language.replace("%ae%", "ä");
        language = language.replace("%Oe%", "Ö");
        language = language.replace("%oe%", "ö");
        language = language.replace("%Ue%", "Ü");
        language = language.replace("%ue%", "ü");
        language = language.replace("%sz%", "ß");
        language = language.replace("%>%", "»");
        language = language.replace("%<%", "«");
        language = language.replace("%*%", "×");
        language = language.replace("%|%", "┃");
        language = language.replace("%->%", "➜");
        language = language.replace("%<3%", "❤");
        return language;
    }

    public String[] getTranslatedLanguages(String section) {
        LinkedList<String> languages = new LinkedList<>();
        if (!(fileConfiguration.isConfigurationSection(section))) {
            return languages.toArray(new String[0]);
        }
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection(section);
        if (configurationSection == null) {
            return languages.toArray(new String[0]);
        }
        for (String language : configurationSection.getKeys(false)) {
            languages.add(getTranslatedLanguage(section + "." + language));
        }
        return languages.toArray(new String[0]);
    }

    public String getReplacedLanguage(String key, PlayerProfile playerProfile) {
        if (!(fileConfiguration.contains(key))) {
            return key;
        }
        String language = getTranslatedLanguage(key);
        UUID uuid = playerProfile.getUuid();
        HashMap<UUID, Map> playerMaps = plugin.getMapManager().getPlayerMaps();
        if (playerMaps.containsKey(uuid)) {
            language = language.replace("%map%", playerMaps.get(uuid).getMapName());
        } else {
            language = language.replace("%map%", "N/A");
        }
        language = formatTime(language, plugin.getMapUpdater().getCurrentSeconds());
        language = language.replace("%kills%", formatNumber(playerProfile.getKills()));
        language = language.replace("%deaths%", formatNumber(playerProfile.getDeaths()));
        language = language.replace("%killstreak%", formatNumber(playerProfile.getKillStreak()));
        return language;
    }

    public String[] getScoreboardLines(String section, PlayerProfile playerProfile) {
        LinkedList<String> languages = new LinkedList<>();
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection(section);
        if (configurationSection == null) {
            return languages.toArray(new String[0]);
        }
        int number = 0;
        String[] empty = {"§b", "§0", "§9", "§3", "§1", "§8", "§2", "§5", "§4", "§6", "§7", "§n", "§l"};
        for (String language : configurationSection.getKeys(false)) {
            String replacedLanguage = getReplacedLanguage(section + "." + language, playerProfile);
            if (empty.length > number) {
                replacedLanguage = replacedLanguage.replace("%blank%", empty[number]);
            }
            languages.add(replacedLanguage);
            number++;
        }
        return languages.toArray(new String[0]);
    }

    public String getLanguageWithPrefix(String key) {
        return getTranslatedLanguage("PREFIX") + getTranslatedLanguage(key);
    }

    public String formatTime(String language, int time) {
        int hours = (time % 86400) / 3600;
        int minutes = ((time % 86400) % 3600) / 60;
        int seconds = ((time % 86400) % 3600) % 60;
        return language.replace("%time%", String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    public String formatNumber(long number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else {
            return new DecimalFormat("0,000").format(number);
        }
    }
}
