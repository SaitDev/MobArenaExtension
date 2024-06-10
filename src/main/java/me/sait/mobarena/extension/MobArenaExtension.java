package me.sait.mobarena.extension;

import com.garbagemule.MobArena.MobArena;
import me.sait.mobarena.core.MobArenaAdapter;
import me.sait.mobarena.core.api.Extension;
import me.sait.mobarena.extension.commands.CommandHandler;
import me.sait.mobarena.extension.config.ConfigManager;
import me.sait.mobarena.extension.config.Constants;
import me.sait.mobarena.extension.integration.discordsrv.DiscordSrvAdapter;
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
    private final static Map<Extension, Boolean> extensions = new HashMap<>();
    private MythicMobsAdapter mythicMobsAdapter;
    private DiscordSrvAdapter discordSrvAdapter;

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
        for (Extension extension : extensions.keySet()) {
            if (Boolean.TRUE.equals(extensions.get(extension))) {
                disableExtension(extension);
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
    public static boolean registerExtension(Extension extension) {
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
            LogHelper.log(e.getMessage(), LogLevel.CRITICAL, e);
            try {
                disableExtension(extension);
            } catch (RuntimeException ex) {
                LogHelper.error(ex.getMessage(), ex);
            }
            return false;
        }
        return true;
    }

    public void reloadExtension(Extension extension) {
        if (Boolean.TRUE.equals(extensions.get(extension))) {
            disableExtension(extension);
        }
        if (extension.shouldEnable()) {
            enableExtension(extension);
        }
    }

    private void reloadExtensions() {
        LogHelper.log("Reloading extensions", LogLevel.DETAIL);
        for (Extension extension : extensions.keySet()) {
            reloadExtension(extension);
        }
    }

    private static void enableExtension(Extension extension) {
        LogHelper.debug("Starting extension {0}", extension.getClass().getSimpleName());
        extension.onEnable();
        extensions.put(extension, true);
    }
    private static void disableExtension(Extension extension) {
        LogHelper.debug("Stopping extension {0}", extension.getClass().getSimpleName());
        extension.onDisable();
        extensions.put(extension, false);
    }

    private void startServices() {
        metricsService = new MetricsService(this, mythicMobsAdapter);
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
        discordSrvAdapter = new DiscordSrvAdapter(mobArenaAdapter);
        registerExtension(discordSrvAdapter);
    }

}
