package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.framework.Arena;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.RequiredArgsConstructor;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.config.ConfigManager;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsAdapter;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class MythicMobListener implements Listener {
    private final MythicMobsAdapter mythicMobsAdapter;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void mythicMobSpawn(MythicMobSpawnEvent event) {
        MobArenaExtension.runTask(() -> {
            ActiveMob am = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(event.getEntity());
            LogHelper.debug("A mythic mob spawned, mythic: " + event.getMobType().getInternalName() +
                    ", entity: " + event.getMobType().getEntityType());

            if (mythicMobsAdapter.getMythicMobEntityManager().isInArena(event.getEntity())) {
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
                if (mythicMobsAdapter.getMythicMobEntityManager().isInArena(parent)) {
                    LogHelper.debug(event.getMobType().getInternalName() + " was spawn by another mythic mob inside mob arena");
                    Arena arena = mythicMobsAdapter.getMythicMobEntityManager().getInArena(parent);
                    if (event.getEntity() instanceof LivingEntity) {
                        arena.getMonsterManager().addMonster((LivingEntity) event.getEntity());
                    } else {
                        LogHelper.error(event.getMobType().getInternalName() + " is not a living entity, currently not compatible with Mob Arena");
                    }
                    mythicMobsAdapter.getMythicMobEntityManager().arenaSpawnMythicMob(arena, event.getEntity());
                    return;
                }
            }

            if (mythicMobsAdapter.getMobArenaAdapter().getArenaAtLocation(event.getLocation()) != null) {
                LogHelper.debug("A non-arena mythic mob spawned inside arena: "  +
                        event.getMobType().getInternalName());
                if (ConfigManager.isBlockNonArenaMythicMob()) {
                    //cant cancel event since we run this on later tick
                    event.getEntity().remove();
                    LogHelper.log("Prevent non-arena mythic mob spawned inside arena: " + event.getMobType().getInternalName(), LogLevel.DETAIL);
//                    event.setCancelled();
                }
            }
        }, 1l);
    }
}
