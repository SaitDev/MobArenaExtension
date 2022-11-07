package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.waves.MACreature;
import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.sait.mobarena.extension.log.LogHelper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class MythicMobCreature extends MACreature {
    private MythicMobsSupport mythicMobsSupport;
    private MythicMob mythicMob;
    private final Boolean isLivingEntity;

    public MythicMobCreature(MythicMobsSupport mythicMobsSupport, MythicMob mythicMob) {
        super(EntityType.fromName(mythicMob.getEntityType()), mythicMob.getInternalName());

        this.mythicMobsSupport = mythicMobsSupport;
        this.mythicMob = mythicMob;

        //TODO deprecated stuff
        EntityType entityType = null;
        try {
            entityType = EntityType.fromName(mythicMob.getEntityType());
            if (entityType == null) {
                entityType = EntityType.valueOf(mythicMob.getEntityType().toUpperCase());
            }
        } catch (Exception e) {
            //MythicMob had some weird added entity type which are diff name with original EntityType
        }
        if (entityType != null) {
            if (LivingEntity.class.isAssignableFrom(entityType.getEntityClass())) {
                isLivingEntity = true;
            } else {
                isLivingEntity = false;
                LogHelper.warn(mythicMob.getInternalName() + " is not a living entity, currently not compatible with Mob Arena");
            }
        } else {
            isLivingEntity = null;
        }
    }

    @Override
    public LivingEntity spawn(Arena arena, World world, Location location) {
        try {
            Entity mMob = MythicBukkit.inst().getAPIHelper().spawnMythicMob(mythicMob, location, 0);
            if (mMob instanceof LivingEntity) {
                mythicMobsSupport.arenaSpawnMythicMob(arena, mMob);
                LivingEntity livingEntity = ((LivingEntity) mMob);

                //temp fix for MA core reset mm hp due to implementation of health-multiplier
                double multiplier = arena.getWaveManager().getCurrent().getHealthMultiplier();
                double maxHp = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                mythicMobsSupport.runTask(() -> {
                    double health = maxHp * multiplier;
                    livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
                    livingEntity.setHealth(Math.max(1D, health));
                }, 1);
                return livingEntity;
            } else {
                LogHelper.error(mythicMob.getInternalName() + " is not a living entity, cant spawn in Mob Arena");
                mMob.remove();
                return null;
            }
        } catch (InvalidMobTypeException e) {
            //mythic mobs were reload but ma creatures can not be unregistered for compatible
            LogHelper.error("Unknown mythic mob: " + mythicMob.getInternalName());
            return null;
        }
    }
}
