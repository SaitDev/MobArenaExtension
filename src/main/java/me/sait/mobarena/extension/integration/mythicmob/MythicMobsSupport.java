package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.waves.MACreature;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.api.Integration;
import me.sait.mobarena.extension.integration.mythicmob.listeners.MobArenaListener;
import me.sait.mobarena.extension.integration.mythicmob.listeners.MythicMobListener;
import me.sait.mobarena.extension.log.LogHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;

public class MythicMobsSupport implements Integration {
    public static final String pluginName = "MythicMobs";
    private MobArenaExtension extension;
    private MobArena mobArena;
    private Map<String, MACreature> originalMACreatures = new HashMap<>();
    private Map<Arena, List<Entity>> cachedMythicMobs = new HashMap();
    private List<MythicMob> registeredMobs = new ArrayList();

    public MythicMobsSupport(MobArenaExtension extension, MobArena mobArena) {
        this.extension = extension;
        this.mobArena = mobArena;
    }

    @Override
    public void onEnable() {
        registerMobs();
        registerListeners();
    }

    @Override
    public void onReload() {}

    @Override
    public void onDisable() {}

    public void arenaSpawnMythicMob(Arena arena, Entity entity) {
        if (!cachedMythicMobs.containsKey(arena) || cachedMythicMobs.get(arena) == null) {
            cachedMythicMobs.put(arena, new ArrayList());
        }

        cachedMythicMobs.get(arena).add(entity);
        LogHelper.debug("A mythic mob spawned inside mob arena: " + entity.getCustomName());
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

    public Arena getArenaAtLocation(Location location) {
        return mobArena.getArenaMaster().getArenaAtLocation(location);
    }

    public void runTask(Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLater(extension, task, delay);
    }

    private void registerListeners() {
        mobArena.getServer().getPluginManager().registerEvents(new MythicMobListener(this), extension);
        mobArena.getServer().getPluginManager().registerEvents(new MobArenaListener(this), extension);
    }

    private void registerMobs() {
        Collection<MythicMob> mmMobs = MythicBukkit.inst().getMobManager().getMobTypes();
        for (MythicMob mob : mmMobs) {
            String creatureKey = mob.getInternalName().toLowerCase().replaceAll("[-_\\.]","");
            MACreature existedCreature = MACreature.fromString(creatureKey);
            if (existedCreature != null) {
                if (existedCreature instanceof MythicMobCreature) {
                    throw new IllegalArgumentException("Can not register mythic mobs with similar name: " + mob.getInternalName());
                } else {
                    originalMACreatures.put(creatureKey, existedCreature);
                }
            }

            MythicMobCreature mmCreature = new MythicMobCreature(this, mob);
            MythicMobCreature.register(creatureKey, mmCreature);
            registeredMobs.add(mob);
            LogHelper.debug("Registered mythic mob: " + mob.getInternalName());
        }
    }
}
