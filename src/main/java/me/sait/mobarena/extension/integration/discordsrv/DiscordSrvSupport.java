package me.sait.mobarena.extension.integration.discordsrv;

import github.scarsz.discordsrv.DiscordSRV;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.sait.mobarena.core.MobArenaAdapter;
import me.sait.mobarena.core.api.Integration;
import me.sait.mobarena.extension.config.ConfigManager;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public class DiscordSrvSupport implements Integration {
    public static final String PLUGIN_NAME = "DiscordSRV";
    @Getter(AccessLevel.PACKAGE)
    private final MobArenaAdapter mobArenaAdapter;
    private DiscordSrvListener discordSrvListener;

    @Override
    public boolean shouldEnable() {
        return ConfigManager.isDiscordSrvEnabled();
    }

    @Override
    public void onEnable() {
        if (!Bukkit.getServer().getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
            LogHelper.log(
                    "DiscordSRV plugin can not be found. Install it or disable discordsrv extension in config",
                    LogLevel.CRITICAL
            );
//                getServer().getPluginManager().disablePlugin(this);
        }

        registerListeners();
    }

    @Override
    public void onDisable() {
        unregisterListeners();
    }

    private void registerListeners() {
        if (discordSrvListener == null) {
            discordSrvListener = new DiscordSrvListener(this);
        }
        DiscordSRV.api.subscribe(discordSrvListener);
    }

    private void unregisterListeners() {
        if (discordSrvListener != null) {
            DiscordSRV.api.unsubscribe(discordSrvListener);
            discordSrvListener = null;
        }
    }
}
