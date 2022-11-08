package me.sait.mobarena.extension;

import com.garbagemule.MobArena.MobArena;
import me.sait.mobarena.core.MobArenaAdapter;
import me.sait.mobarena.core.api.Integration;
import me.sait.mobarena.extension.commands.CommandHandler;
import me.sait.mobarena.extension.config.ConfigManager;
import me.sait.mobarena.extension.config.Constants;
import me.sait.mobarena.extension.integration.discordsrv.DiscordSrvSupport;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsAdapter;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import me.sait.mobarena.extension.services.MetricsService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class MobArenaExtension extends JavaPlugin {
    private ConfigManager configManager;
    private MetricsService metricsService;
    private MobArenaAdapter mobArenaAdapter;

    /**
     * integrated module - running
     */
    private static Map<Integration, Boolean> extensions = new HashMap<>();
    private MythicMobsAdapter mythicMobsAdapter;
    private DiscordSrvSupport discordSrvSupport;

    public static MobArenaExtension getPlugin() {
        return getPlugin(MobArenaExtension.class);
    }

    public static void runTask(Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLater(getPlugin(), task, delay);
    }

    @Override
    public void onLoad() {
        setupConfig();
    }

    @Override
    public void onEnable() {
        initMobArena();
        initMythicMob();
        initDiscordSrv();

        startServices();

        getCommand("mobarenaextension").setExecutor(new CommandHandler(this));
    }

    @Override
    public void onDisable() {
        for (Integration integration : extensions.keySet()) {
            if (Boolean.TRUE.equals(extensions.get(integration))) {
                disableExtension(integration);
            }
        }
    }

    public void reload() {
        loadDefaultConfig();
        reloadConfig();
        configManager.reload();
        reloadExtensions();
    }

    private void setupConfig() {
        loadDefaultConfig();
        configManager = new ConfigManager(this);
    }

    private void loadDefaultConfig() {
        boolean pluginFolderNotExists = !getDataFolder().exists();
        File file = new File(getDataFolder(), "config.yml");

        if (pluginFolderNotExists || !file.exists()) {
            LogHelper.info("config.yml not found, creating new one!");
            saveDefaultConfig();
        }
    }

    /**
     *
     * @param extension
     * @return whether its successfully registered (but not mean enabled or not. @see MobArenaExtension.extensions)
     */
    public static boolean registerExtension(Integration extension) {
        if (extension == null) return false;

        if (extensions.containsKey(extension)) {
            throw new IllegalStateException("Extension has been already registered");
        }

        extensions.put(extension, false);
        try {
            if (extension.shouldEnable()) {
                enableExtension(extension);
            }
        } catch (RuntimeException e) {
            LogHelper.log(null, LogLevel.CRITICAL, e);
            try {
                disableExtension(extension);
            } catch (RuntimeException ex) {
                LogHelper.error(null, ex);
            }
            return false;
        }
        return true;
    }

    private void reloadExtensions() {
        LogHelper.log("Reloading extensions", LogLevel.DETAIL);
        for (Integration integration : extensions.keySet()) {
            if (Boolean.TRUE.equals(extensions.get(integration))) {
                disableExtension(integration);
            }
            if (integration.shouldEnable()) {
                enableExtension(integration);
            }
        }
    }

    private static void enableExtension(Integration extension) {
        LogHelper.debug("Starting extension " + extension.getClass().getSimpleName());
        extension.onEnable();
        extensions.put(extension, true);
    }
    private static void disableExtension(Integration extension) {
        LogHelper.debug("Stopping extension " + extension.getClass().getSimpleName());
        extension.onDisable();
        extensions.put(extension, false);
    }

    private void startServices() {
        metricsService = new MetricsService();
        metricsService.start();
    }

    private void initMobArena() {
        MobArena mobArena = (MobArena) getServer().getPluginManager().getPlugin(Constants.MOB_ARENA_PLUGIN_NAME);
        if (mobArena == null || !mobArena.isEnabled()) {
            throw new NullPointerException("This extension requires core plugin MobArena installed and enabled");
        }
        mobArenaAdapter = new MobArenaAdapter(mobArena);
        registerExtension(mobArenaAdapter);
    }

    private void initMythicMob() {
        mythicMobsAdapter = new MythicMobsAdapter(this, mobArenaAdapter);
        registerExtension(mythicMobsAdapter);
    }

    private void initDiscordSrv() {
        discordSrvSupport = new DiscordSrvSupport(mobArenaAdapter);
        registerExtension(discordSrvSupport);
    }

    private void disableDiscordSrv() {
        if (configManager.isDiscordSrvEnabled() &&
                getServer().getPluginManager().isPluginEnabled(DiscordSrvSupport.PLUGIN_NAME) &&
                discordSrvSupport != null
        ) {
            discordSrvSupport.onDisable();
            extensions.remove(discordSrvSupport);
        }
    }
}
