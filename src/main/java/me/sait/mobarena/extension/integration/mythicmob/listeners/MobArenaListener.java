package me.sait.mobarena.extension.integration.mythicmob.listeners;

import com.garbagemule.MobArena.events.ArenaEndEvent;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsSupport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MobArenaListener implements Listener {
    private MythicMobsSupport mythicMobsSupport;

    public MobArenaListener(MythicMobsSupport mythicMobsSupport) {
        this.mythicMobsSupport = mythicMobsSupport;
    }

    @EventHandler(ignoreCancelled = true)
    public void arenaEnd(ArenaEndEvent event) {
        mythicMobsSupport.arenaEnd(event.getArena());
    }
}
