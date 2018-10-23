package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.integration.mythicmob.listeners.MobArenaListener;
import me.sait.mobarena.extension.integration.mythicmob.listeners.MythicMobListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.*;

public class MythicMobsSupport {
    private MobArenaExtension extension;
    private MobArena mobArena;
    private Map<Arena, List<Entity>> cachedMythicMobs = new HashMap();

    public MythicMobsSupport(MobArenaExtension extension, MobArena mobArena) {
        this.extension = extension;
        this.mobArena = mobArena;
    }

    public void registerListeners() {
        mobArena.getServer().getPluginManager().registerEvents(new MythicMobListener(this), extension);
        mobArena.getServer().getPluginManager().registerEvents(new MobArenaListener(this), extension);
    }

    public void registerMobs() {
        Collection<MythicMob> mmMobs = MythicMobs.inst().getMobManager().getMobTypes();
//        System.out.println(MythicMobs.inst().getMobManager().get);
        for (MythicMob mob : mmMobs) {
//            System.out.println(mob.getDisplayName());
//            System.out.println(mob.getFileName());
//            System.out.println(mob.getInternalName());
//            System.out.println(mob.getEntityType());
//            System.out.println(mob.getMythicEntity());
            new MythicMobCreature(this, mob);
        }
    }

    public void arenaSpawnMythicMob(Arena arena, Entity entity) {
        if (!cachedMythicMobs.containsKey(arena) || cachedMythicMobs.get(arena) == null) {
            cachedMythicMobs.put(arena, new ArrayList());
        }

        cachedMythicMobs.get(arena).add(entity);
    }

    public void arenaEnd(Arena arena) {
        if (cachedMythicMobs.containsKey(arena)) {
            cachedMythicMobs.remove(arena);
        }
    }

    public boolean isInArena(Entity entity) {
        for (List<Entity> entities : cachedMythicMobs.values()) {
            if (entities.contains(entity)) {
                return true;
            }
        }
        return false;
    }

    public Arena getInArena(Entity entity) {
        for (Map.Entry<Arena, List<Entity>> entry : cachedMythicMobs.entrySet()) {
            if (entry.getValue().contains(entity)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void runTask(Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLater(extension, task, delay);
    }
}
