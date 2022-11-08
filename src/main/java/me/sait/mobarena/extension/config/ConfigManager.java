package me.sait.mobarena.extension.config;

import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.log.LogHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

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

    //Extension

    public static Boolean isMythicMobEnabled() {
        if (!initialized) return null;
        return extensionSettings().getBoolean(MYTHIC_MOB_PREFIX + ENABLE_SETTING, false);
    }

    public static Boolean isBlockNonArenaMythicMob() {
        if (!initialized) return null;
        return extensionSettings().getBoolean(MYTHIC_MOB_PREFIX + ".block-non-arena-mythic-mob", true);
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
