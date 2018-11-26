package me.sait.mobarena.extension.integration.mythicmob.listeners;

import com.garbagemule.MobArena.framework.Arena;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsSupport;
import me.sait.mobarena.extension.log.LogHelper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MythicMobListener implements Listener {
    private MythicMobsSupport mythicMobsSupport;

    public MythicMobListener(MythicMobsSupport mythicMobsSupport) {
        this.mythicMobsSupport = mythicMobsSupport;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void mythicMobSpawn(MythicMobSpawnEvent event) {
        mythicMobsSupport.runTask(() -> {
            ActiveMob am = MythicMobs.inst().getAPIHelper().getMythicMobInstance(event.getEntity());
            LogHelper.debug("A mythic mob spawned, mythic: " + event.getMobType().getInternalName() +
                    ", entity: " + event.getMobType().getEntityType());
            if (am == null) {
                LogHelper.error("Could not found the entity object of spawned mythic mob");
            }
            if (am != null && am.getParent() != null) {
                Entity parent = am.getParent().getEntity().getBukkitEntity();
                if (mythicMobsSupport.isInArena(parent)) {
                    LogHelper.debug("Mob was spawn by another mythic mob inside mob arena");
                    Arena arena = mythicMobsSupport.getInArena(parent);
                    if (parent instanceof LivingEntity) {
                        arena.getMonsterManager().addMonster((LivingEntity) parent);
                    }
                    mythicMobsSupport.arenaSpawnMythicMob(arena, event.getEntity());
                }
            }
        }, 1l);
    }
}
