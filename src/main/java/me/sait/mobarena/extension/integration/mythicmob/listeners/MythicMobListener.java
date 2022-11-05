package me.sait.mobarena.extension.integration.mythicmob.listeners;

import com.garbagemule.MobArena.framework.Arena;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.sait.mobarena.extension.config.ConfigManager;
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
            ActiveMob am = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(event.getEntity());
            LogHelper.debug("A mythic mob spawned, mythic: " + event.getMobType().getInternalName() +
                    ", entity: " + event.getMobType().getEntityType());

            if (mythicMobsSupport.isInArena(event.getEntity())) {
                //this is arena mob. no more checking need
                return;
            }

            if (am == null) {
                LogHelper.error("Could not found the entity object of spawned mythic mob");
                return;
            }

            if (am.getParent() != null) {
                Entity parent = am.getParent().getEntity().getBukkitEntity();
                ActiveMob parentMM = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(parent);
                if (parentMM != null) {
                    LogHelper.debug(event.getMobType().getInternalName() + " spawned via skill Summon by " + parentMM.getType().getInternalName());
                }
                if (mythicMobsSupport.isInArena(parent)) {
                    LogHelper.debug(event.getMobType().getInternalName() + " was spawn by another mythic mob inside mob arena");
                    Arena arena = mythicMobsSupport.getInArena(parent);
                    if (event.getEntity() instanceof LivingEntity) {
                        arena.getMonsterManager().addMonster((LivingEntity) event.getEntity());
                    } else {
                        LogHelper.error(event.getMobType().getInternalName() + " is not a living entity, currently not compatible with Mob Arena");
                    }
                    mythicMobsSupport.arenaSpawnMythicMob(arena, event.getEntity());
                    return;
                }
            }

            if (mythicMobsSupport.getArenaAtLocation(event.getLocation()) != null) {
                LogHelper.debug("A non-arena mythic mob spawned inside arena: "  +
                        event.getMobType().getInternalName());
                if (ConfigManager.isBlockNonArenaMythicMob()) {
                    //cant cancel event since we run this on later tick
                    event.getEntity().remove();
//                    event.setCancelled();
                }
            }
        }, 1l);
    }
}
