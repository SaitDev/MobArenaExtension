package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.waves.MACreature;
import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import lombok.val;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.log.LogHelper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class MythicMobCreature extends MACreature {
    private final MythicMobsAdapter mythicMobsAdapter;
    private final MythicMob mythicMob;
    @Getter
    private final boolean isLivingEntity;

    public MythicMobCreature(MythicMobsAdapter mythicMobsAdapter, MythicMob mythicMob) {
        super(toEntityType(mythicMob), mythicMob.getInternalName());

        this.mythicMobsAdapter = mythicMobsAdapter;
        this.mythicMob = mythicMob;

        EntityType entityType = toEntityType(mythicMob);
        if (LivingEntity.class.isAssignableFrom(entityType.getEntityClass())) {
            isLivingEntity = true;
        } else {
            isLivingEntity = false;
            LogHelper.error(mythicMob.getInternalName() + " is not a living entity, currently not compatible with Mob Arena");
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
                LogHelper.error(mythicMob.getInternalName() + " is not a living entity, cant spawn in Mob Arena");
                mMob.remove();
                throw new UnsupportedOperationException("Not a living entity: " + mythicMob.getInternalName());
            }
        } catch (InvalidMobTypeException e) {
            //mythic mobs were reload but MA creatures can not be unregistered for compatible
            LogHelper.error("Unknown mythic mob: " + mythicMob.getInternalName());
            return null;
        }
    }

    private static EntityType toEntityType(MythicMob mythicMob) {
        String mmEntityType = mythicMob.getEntityType() != null
                ? mythicMob.getEntityType()
                : mythicMob.getInternalName();
        //MythicEntityType ~ EntityType
        //BukkitEntityType.getMythicEntity()
        //MythicMob had some weird added entity type which are diff name with original EntityType
        /** TODO manual mapping
         * BABY_DROWNED
         * BABY_HUSK
         * BABY_PIGLIN
         * BABY_PIGLIN_BRUTE
         * BABY_PIG_ZOMBIE
         * BABY_PIG_ZOMBIE_VILLAGER
         * BABY_ZOGLIN
         * BABY_ZOMBIE
         * BABY_ZOMBIE_VILLAGER
         * CUSTOM
         * ITEM
         * PIG_ZOMBIE
         * PIG_ZOMBIE_VILLAGER
         * VINDIOCELOTOR
         */
        val entityType = EntityType.valueOf(mmEntityType.toUpperCase());
        return entityType;
    }
}
