package me.sait.mobarena.extension;

import com.garbagemule.MobArena.MobArena;
import me.sait.mobarena.extension.config.ConfigManager;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsSupport;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MobArenaExtension extends JavaPlugin {
    private ConfigManager configManager;
    private MobArena mobArena;
    private String mobArenaPluginName = "MobArena";
    private MythicMobsSupport mythicMobsSupport;

    public static MobArenaExtension getPlugin() {
        return getPlugin(MobArenaExtension.class);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        setupConfig();
        initMobArena();
        initMythicMob();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //TODO - clear browser history before leaving computer
    }

    private void setupConfig() {
        loadDefaultConfig();
        configManager = new ConfigManager(this);
    }

    private void loadDefaultConfig() {
        boolean pluginFolderNotexists = !getDataFolder().exists();
        if (pluginFolderNotexists) {
            getDataFolder().mkdirs();
        }
        File file = new File(getDataFolder(), "config.yml");
        if (pluginFolderNotexists || !file.exists()) {
            LogHelper.info("config.yml not found, creating new one!");
            saveDefaultConfig();
        }
    }

    private void reload() {
        loadDefaultConfig();
        reloadConfig();
        configManager.reload();
    }

    private void initMobArena() {
        mobArena = (MobArena) getServer().getPluginManager().getPlugin(mobArenaPluginName);
    }

    private void initMythicMob() {
        if (configManager.isMythicMobEnabled()) {
            LogHelper.log("Init mythic mob", LogLevel.DETAIL);
            if (getServer().getPluginManager().getPlugin(MythicMobsSupport.pluginName) == null) {
                LogHelper.log(
                        "MythicMobs plugin can not be found. Install it or disable mythicmob extension in config",
                        LogLevel.CRITICAL
                );
                getServer().getPluginManager().disablePlugin(this);
            }

            mythicMobsSupport = new MythicMobsSupport(this, mobArena);
            mythicMobsSupport.registerMobs();
            mythicMobsSupport.registerListeners();

            try {
                mobArena.reload(); //so that mob arena can parse mobs from mythicmobs
            } catch (RuntimeException error) {}
        }
    }
}
