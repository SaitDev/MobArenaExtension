package me.sait.mobarena.extension;

import com.garbagemule.MobArena.MobArena;
import me.sait.mobarena.extension.config.ConfigManager;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsSupport;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MobArenaExtension extends JavaPlugin {
    private ConfigManager configManager;
    private MobArena mobArena;
    private String mobArenaPluginName = "MobArena";
    private String mythicMobPluginName = "MythicMobs";

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
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            getLogger().info("config.yml not found, creating new one!");
            saveDefaultConfig();
        }
        configManager = new ConfigManager(this);
    }

    private void initMobArena() {
        mobArena = (MobArena) getServer().getPluginManager().getPlugin(mobArenaPluginName);
    }

    private void initMythicMob() {
        getLogger().info("Init mythic mob");
        if (configManager.isMythicMobEnabled()) {
            if (getServer().getPluginManager().getPlugin(mythicMobPluginName) == null) {
                getLogger().severe("MythicMobs plugin can not be found. Install it or disable mythicmob extension in config");
                getServer().getPluginManager().disablePlugin(this);
            }

            MythicMobsSupport mythicMobsSupport = new MythicMobsSupport(mobArena);
            mythicMobsSupport.registerMobs();
        }
    }
}
