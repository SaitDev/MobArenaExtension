package me.sait.mobarena.extension.config;

import me.sait.mobarena.extension.MobArenaExtension;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private static MobArenaExtension plugin;
    private static FileConfiguration fileConfig;
    private static boolean initialized;
    private static String bungeeCordPrefix = "bungeecord";

    public ConfigManager(MobArenaExtension plugin) {
        this.plugin = plugin;
        this.fileConfig = plugin.getConfig();
        initialized = true;
    }

    //General

    //Extension

    public static Boolean isBungeeCordEnabled() {
        return !initialized ? null : extensionSettings().getBoolean(bungeeCordPrefix + ".enable", false);
    }

    public static Boolean isMythicMobEnabled() {
        if (!initialized) return null;
        return extensionSettings().getBoolean("mythicmob.enable", false);
    }

    //Section

    private static ConfigurationSection generalSettings() {
        return fileConfig.getConfigurationSection("general");
    }

    private static ConfigurationSection extensionSettings() {
        return fileConfig.getConfigurationSection("extension");
    }
}
