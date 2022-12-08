package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.waves.MACreature;
import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.api.mobs.entities.MythicEntityType;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.log.LogHelper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class MythicMobCreature extends MACreature {
    private static final Map<String, EntityType> MYTHIC_ENTITIES = new HashMap<>();
    private final MythicMobsAdapter mythicMobsAdapter;
    private final MythicMob mythicMob;
    @Getter
    private final boolean isLivingEntity;
    static {
        MYTHIC_ENTITIES.put(MythicEntityType.BABY_DROWNED.name(), EntityType.DROWNED);
        MYTHIC_ENTITIES.put(MythicEntityType.BABY_HUSK.name(), EntityType.HUSK);
        MYTHIC_ENTITIES.put(MythicEntityType.BABY_PIGLIN.name(), EntityType.PIGLIN);
        MYTHIC_ENTITIES.put(MythicEntityType.BABY_PIGLIN_BRUTE.name(), EntityType.PIGLIN_BRUTE);
        MYTHIC_ENTITIES.put(MythicEntityType.BABY_PIG_ZOMBIE.name(), EntityType.ZOMBIFIED_PIGLIN);
        MYTHIC_ENTITIES.put(MythicEntityType.BABY_PIG_ZOMBIE_VILLAGER.name(), EntityType.ZOMBIFIED_PIGLIN);
        MYTHIC_ENTITIES.put(MythicEntityType.BABY_ZOGLIN.name(), EntityType.ZOGLIN);
        MYTHIC_ENTITIES.put(MythicEntityType.BABY_ZOMBIE.name(), EntityType.ZOMBIE);
        MYTHIC_ENTITIES.put(MythicEntityType.BABY_ZOMBIE_VILLAGER.name(), EntityType.ZOMBIE_VILLAGER);
        MYTHIC_ENTITIES.put(MythicEntityType.PIG_ZOMBIE.name(), EntityType.ZOMBIFIED_PIGLIN);
        MYTHIC_ENTITIES.put(MythicEntityType.PIG_ZOMBIE_VILLAGER.name(), EntityType.ZOMBIFIED_PIGLIN);
        MYTHIC_ENTITIES.put(MythicEntityType.VINDIOCELOTOR.name(), EntityType.VINDICATOR);
    }

    public MythicMobCreature(MythicMobsAdapter mythicMobsAdapter, MythicMob mythicMob) {
        super(toEntityType(mythicMob), mythicMob.getInternalName());

        this.mythicMobsAdapter = mythicMobsAdapter;
        this.mythicMob = mythicMob;

        EntityType entityType = toEntityType(mythicMob);
        if (entityType != null && LivingEntity.class.isAssignableFrom(entityType.getEntityClass())) {
            isLivingEntity = true;
        } else {
            isLivingEntity = false;
            if (entityType == null) {
                LogHelper.warn("No entity type found for {0} - {1}", mythicMob.getInternalName(), mythicMob.getEntityType().toString());
            } else {
                LogHelper.warn("{0} is not a living entity, currently not compatible with Mob Arena", mythicMob.getInternalName());
            }
        }
    }

    public static String getCreatureKey(MythicMob mob) {
        return mob.getInternalName().toLowerCase().replaceAll("[-_\\.]","");
    }

    @Override
    public LivingEntity spawn(Arena arena, World world, Location location) {
        try {
            Entity mMob = MythicBukkit.inst().getAPIHelper().spawnMythicMob(mythicMob, location, 0);
            if (mMob instanceof LivingEntity) {
                mythicMobsAdapter.getMythicMobEntityManager().arenaSpawnMythicMob(arena, mMob);
                LivingEntity livingEntity = ((LivingEntity) mMob);

                //temp fix for MA core reset mm hp due to implementation of health-multiplier
                double multiplier = arena.getWaveManager().getCurrent().getHealthMultiplier();
                double maxHp = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                MobArenaExtension.runTask(() -> {
                    double health = maxHp * multiplier;
                    livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
                    livingEntity.setHealth(Math.max(1D, health));
                }, 1);
                return livingEntity;
            } else {
                mMob.remove();
                throw new UnsupportedOperationException(mythicMob.getInternalName() + " is not a living entity, not supported in Mob Arena");
            }
        } catch (InvalidMobTypeException e) {
            //mythic mobs were reload but MA creatures can not be unregistered for compatible
            LogHelper.error("Unknown mythic mob: {0}", mythicMob.getInternalName());
            return null;
        }
    }

    private static EntityType toEntityType(MythicMob mythicMob) {
        String mmEntityType = mythicMob.getEntityType() != null
                ? mythicMob.getEntityType().toString() //5.2
                : mythicMob.getInternalName();
        //MythicEntityType ~ EntityType
        //BukkitEntityType.getMythicEntity()
        //MythicMob had some weird added entity type which are diff name with original EntityType
        /**
         * BABY_DROWNED
         * BABY_HUSK
         * BABY_PIGLIN
         * BABY_PIGLIN_BRUTE
         * BABY_PIG_ZOMBIE
         * BABY_PIG_ZOMBIE_VILLAGER
         * BABY_ZOGLIN
         * BABY_ZOMBIE
         * BABY_ZOMBIE_VILLAGER
         * PIG_ZOMBIE
         * PIG_ZOMBIE_VILLAGER
         * VINDIOCELOTOR
         * CUSTOM
         * ITEM
         */
        EntityType entityType = null;
        try {
            entityType = EntityType.valueOf(mmEntityType.toUpperCase());
        } catch (IllegalArgumentException e) {
            entityType = MYTHIC_ENTITIES.get(mmEntityType);
        }
        //null EntityType doesnt affect MobArena core, but may affect other plugins that integrate directly with MA
        return entityType;
    }
}
