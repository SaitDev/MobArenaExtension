package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.framework.Arena;
import lombok.RequiredArgsConstructor;
import me.sait.mobarena.extension.log.LogHelper;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class MythicMobEntityManager {
    private final MythicMobsAdapter mythicMobsAdapter;
    private final Map<Arena, List<Entity>> cachedMythicMobs = new HashMap();

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
}
