package me.sait.mobarena.extension.config;

import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.log.LogHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private static MobArenaExtension plugin;
    private static FileConfiguration fileConfig;
    private static boolean initialized;
    private static final String mythicMobPrefix = "mythicmob";
    private static final String placeholderAPIPrefix = "placeholderapi";
    private static final String discordSrvPrefix = "discordsrv";
    private static final String enableSetting = ".enable";

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

    //Extension

    public static Boolean isMythicMobEnabled() {
        if (!initialized) return null;
        return extensionSettings().getBoolean(mythicMobPrefix + enableSetting, false);
    }

    public static Boolean isPlaceholderAPIEnabled() {
        if (!initialized) return null;
        return extensionSettings().getBoolean(placeholderAPIPrefix + enableSetting, false);
    }

    public static Boolean isDiscordSrvEnabled() {
        if (!initialized) return null;
        return extensionSettings().getBoolean(discordSrvPrefix + enableSetting, false);
    }

    //Section

    private static ConfigurationSection generalSettings() {
        return fileConfig.getConfigurationSection("general");
    }

    private static ConfigurationSection extensionSettings() {
        return fileConfig.getConfigurationSection("extension");
    }
}
