package me.sait.mobarena.extension.services;

import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import org.bstats.bukkit.Metrics;

public class MetricsService {
    private MobArenaExtension extension;
    private Metrics metrics;

    public MetricsService() {
        extension = MobArenaExtension.getPlugin();
    }

    public void start() {
        if (extension == null || !extension.isEnabled()) {
            return;
        }

        LogHelper.log("Starting Metrics", LogLevel.DETAIL);
        metrics = new Metrics(extension);
    }

    public void stop() {
        //doesnt need to stop metric service, just disable the extension plugin
    }
}
