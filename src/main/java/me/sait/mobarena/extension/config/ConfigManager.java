package me.sait.mobarena.extension.config;

import lombok.val;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.utils.CommonUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public class ConfigManager {
    private static MobArenaExtension plugin;
    private static FileConfiguration fileConfig;
    private static boolean initialized;
    private static final String MYTHIC_MOB_PREFIX = "mythicmob";
    private static final String DISCORD_SRV_PREFIX = "discordsrv";
    private static final String ENABLE_SETTING = ".enable";

    public ConfigManager(MobArenaExtension plugin) {
        this.plugin = plugin;
        this.fileConfig = plugin.getConfig();
        initialized = true;
    }

    public void reload() {
        this.fileConfig = plugin.getConfig();
    }

    public static boolean isInitialized() {
        return initialized;
    }

    //General

    public static Integer getLogLevel() {
        if (!initialized) return null;
        return generalSettings().getInt("log-level", LogHelper.defaulLevel.ordinal());
    }

    public static List<?> getAsList(ConfigurationSection section, String path) {
        val properties = section.getList(path);
        if (CommonUtils.isEmptyList(properties)) {
            val property = section.get(path);
            if (property != null) return Arrays.asList(property);
        }
        return properties;
    }

    //Extension

    public static Boolean isMythicMobEnabled() {
        if (!initialized) return null;
        return extensionSettings().getBoolean(MYTHIC_MOB_PREFIX + ENABLE_SETTING, false);
    }

    public static Boolean isBlockNonArenaMythicMob() {
        if (!initialized) return null;
        return extensionSettings().getBoolean(MYTHIC_MOB_PREFIX + ".block-non-arena-mythic-mob", true);
    }

    public static List<String> getWorldsBlockNonArenaMythicMob() {
        return (List<String>) getAsList(extensionSettings().getConfigurationSection(MYTHIC_MOB_PREFIX), "block-non-arena-mythic-mob-on-world");
    }

    public static Boolean isDiscordSrvEnabled() {
        if (!initialized) return null;
        return extensionSettings().getBoolean(DISCORD_SRV_PREFIX + ENABLE_SETTING, false);
    }

    //Section

    private static ConfigurationSection generalSettings() {
        return fileConfig.getConfigurationSection("general");
    }

    private static ConfigurationSection extensionSettings() {
        return fileConfig.getConfigurationSection("extension");
    }
}
