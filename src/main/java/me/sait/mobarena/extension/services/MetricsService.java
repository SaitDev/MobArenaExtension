package me.sait.mobarena.extension.services;

import lombok.RequiredArgsConstructor;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.config.ConfigManager;
import me.sait.mobarena.extension.config.Constants;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import org.bstats.bukkit.Metrics;

import java.util.Map;

@RequiredArgsConstructor
public class MetricsService {
    private final MobArenaExtension extension;
    private Metrics metrics;

    public void start() {
        if (extension == null || !extension.isEnabled()) {
            return;
        }

        LogHelper.log("Starting Metrics", LogLevel.DETAIL);
        metrics = new Metrics(extension, Constants.MA_EX_BSTATS_ID);
    }

    public void stop() {
        //doesnt need to stop metric service, just disable the extension plugin
    }
}
