package me.sait.mobarena.extension;

import com.garbagemule.MobArena.MobArena;
import me.sait.mobarena.extension.config.ConfigManager;
import me.sait.mobarena.extension.config.Constants;
import me.sait.mobarena.extension.integration.discordsrv.DiscordSrvSupport;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsSupport;
import me.sait.mobarena.extension.integration.placeholderapi.PlaceholderAPISupport;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MobArenaExtension extends JavaPlugin {
    private ConfigManager configManager;
    private MobArena mobArena;
    private MythicMobsSupport mythicMobsSupport;
    private PlaceholderAPISupport placeholderAPISupport;
    private DiscordSrvSupport discordSrvSupport;

    public static MobArenaExtension getPlugin() {
        return getPlugin(MobArenaExtension.class);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        setupConfig();
        initMobArena();
        initMythicMob();
        initPlaceholderAPI();
        initDiscordSrv();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //TODO - unregister listeners
        disableDiscordSrv();
    }

    private void setupConfig() {
        loadDefaultConfig();
        configManager = new ConfigManager(this);
    }

    private void loadDefaultConfig() {
        boolean pluginFolderNotExists = !getDataFolder().exists();
        if (pluginFolderNotExists) {
            getDataFolder().mkdirs();
        }
        File file = new File(getDataFolder(), "config.yml");
        if (pluginFolderNotExists || !file.exists()) {
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
        mobArena = (MobArena) getServer().getPluginManager().getPlugin(Constants.MOB_ARENA_PLUGIN_NAME);
        if (mobArena == null || !mobArena.isEnabled()) {
            throw new NullPointerException("This extension requires core plugin MobArena installed and enabled");
        }
    }

    private void initMythicMob() {
        if (configManager.isMythicMobEnabled()) {
            LogHelper.log("Init mythic mob", LogLevel.DETAIL);
            if (!getServer().getPluginManager().isPluginEnabled(MythicMobsSupport.pluginName)) {
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

    private void initPlaceholderAPI() {
        if (configManager.isPlaceholderAPIEnabled()) {
            LogHelper.log("Init placeholder api", LogLevel.DETAIL);
            if (!getServer().getPluginManager().isPluginEnabled(PlaceholderAPISupport.pluginName)) {
                LogHelper.log(
                        "PlaceholderAPI plugin can not be found. Install it or disable placeholderapi extension in config",
                        LogLevel.CRITICAL
                );
                getServer().getPluginManager().disablePlugin(this);
            }

            placeholderAPISupport = new PlaceholderAPISupport(this, mobArena);
            placeholderAPISupport.register();
        }
    }

    private void initDiscordSrv() {
        if (configManager.isDiscordSrvEnabled()) {
            LogHelper.log("Init discordsrv", LogLevel.DETAIL);
            if (!getServer().getPluginManager().isPluginEnabled(DiscordSrvSupport.pluginName)) {
                LogHelper.log(
                        "DiscordSRV plugin can not be found. Install it or disable discordsrv extension in config",
                        LogLevel.CRITICAL
                );
                getServer().getPluginManager().disablePlugin(this);
            }

            discordSrvSupport = new DiscordSrvSupport(mobArena);
            discordSrvSupport.onEnable();
        }
    }

    private void disableDiscordSrv() {
        if (configManager.isDiscordSrvEnabled() &&
                getServer().getPluginManager().isPluginEnabled(DiscordSrvSupport.pluginName) &&
                discordSrvSupport != null
        ) {
            discordSrvSupport.onDisable();
        }
    }
}
