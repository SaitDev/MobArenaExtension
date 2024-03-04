package me.sait.mobarena.extension.services;

import lombok.RequiredArgsConstructor;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.config.ConfigManager;
import me.sait.mobarena.extension.config.Constants;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsAdapter;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bstats.charts.SimplePie;

import java.util.Map;

@RequiredArgsConstructor
public class MetricsService {
    private final String CHART_MODULE_USAGE = "enabledModulePie";
    private final String CHART_MYTHICMOB_VERSIONS = "mythicMobsVersion";
    private final String MODULE_MYTHICMOB = "MythicMobs";
    private final String MODULE_DISCORDSRV = "DiscordSrv";

    private final MobArenaExtension extension;
    private final MythicMobsAdapter mythicMobsAdapter;
    private Metrics metrics;

    public void start() {
        if (extension == null || !extension.isEnabled()) {
            return;
        }

        LogHelper.log("Starting Metrics", LogLevel.DETAIL);
        metrics = new Metrics(extension, Constants.MA_EX_BSTATS_ID);
        metrics.addCustomChart(new AdvancedPie(CHART_MODULE_USAGE, this::enabledModules));
        metrics.addCustomChart(new SimplePie(CHART_MYTHICMOB_VERSIONS, this::mythicMobsVersion));
    }

    public void stop() {
        //doesnt need to stop metric service, just disable the extension plugin
    }
    
    private Map<String, Integer> enabledModules() {
        return Map.of(
                MODULE_MYTHICMOB, ConfigManager.isMythicMobEnabled() ? 1 : 0,
                MODULE_DISCORDSRV, ConfigManager.isDiscordSrvEnabled() ? 1 : 0
        );
    }

    private String mythicMobsVersion() {
        try {
            return mythicMobsAdapter.getMythicMobsVersion();
        } catch (Exception e) {
            LogHelper.error("Can not obtain MythicMobs plugin's version", e);
            return null;
        }
    }
}
