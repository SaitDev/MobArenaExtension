package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.events.ArenaEndEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class MobArenaMMListener implements Listener {
    private final MythicMobsAdapter mythicMobsAdapter;

    @EventHandler(ignoreCancelled = true)
    public void arenaEnd(ArenaEndEvent event) {
        mythicMobsAdapter.getMythicMobEntityManager().arenaEnd(event.getArena());
    }
}
