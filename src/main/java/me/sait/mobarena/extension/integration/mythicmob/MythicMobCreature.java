package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.waves.MACreature;
import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.api.mobs.entities.MythicEntityType;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.AccessLevel;
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

import static me.sait.mobarena.extension.utils.CommonUtils.ignoreException;

public class MythicMobCreature extends MACreature {
    private static final Map<String, EntityType> MYTHIC_ENTITIES = new HashMap<>();
    private final MythicMobsAdapter mythicMobsAdapter;
    @Getter(AccessLevel.PACKAGE)
    private final MythicMob mythicMob;
    @Getter
    private final boolean isLivingEntity;
    static {
        //attempt to keep extension running if entity is removed in the future
        MYTHIC_ENTITIES.put("BABY_DROWNED", EntityType.DROWNED);
        MYTHIC_ENTITIES.put("BABY_HUSK", EntityType.HUSK);
        MYTHIC_ENTITIES.put("BABY_PIGLIN", EntityType.PIGLIN);
        MYTHIC_ENTITIES.put("BABY_PIGLIN_BRUTE", EntityType.PIGLIN_BRUTE);
        MYTHIC_ENTITIES.put("BABY_PIG_ZOMBIE", EntityType.ZOMBIFIED_PIGLIN);
        MYTHIC_ENTITIES.put("BABY_PIG_ZOMBIE_VILLAGER", EntityType.ZOMBIFIED_PIGLIN);
        MYTHIC_ENTITIES.put("BABY_ZOGLIN", EntityType.ZOGLIN);
        MYTHIC_ENTITIES.put("BABY_ZOMBIE", EntityType.ZOMBIE);
        MYTHIC_ENTITIES.put("BABY_ZOMBIE_VILLAGER", EntityType.ZOMBIE_VILLAGER);
        MYTHIC_ENTITIES.put("ZOMBIFIED_PIGLIN_VILLAGER", EntityType.ZOMBIFIED_PIGLIN);
        MYTHIC_ENTITIES.put("VINDIOCELOTOR", EntityType.VINDICATOR);        /**
         * CUSTOM
         * ITEM
         */
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
        //use bellow code to verify
        //for (io.lumine.mythic.api.mobs.entities.MythicEntityType value : io.lumine.mythic.api.mobs.entities.MythicEntityType.values()) {
        //            try {
        //                EntityType.valueOf(value.name().toUpperCase());
        //            } catch (Exception e) {
        //                LogHelper.error(value.name(), e);
        //            }
        //        }
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
