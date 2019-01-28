package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.waves.MACreature;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class MythicMobCreature extends MACreature {
    private MythicMobsSupport mythicMobsSupport;
    private MythicMob mythicMob;
    private final boolean isLivingEntity;

    public MythicMobCreature(MythicMobsSupport mythicMobsSupport, MythicMob mythicMob) {
        super(mythicMob.getInternalName().toLowerCase().replaceAll("[-_\\.]",""),
                EntityType.fromName(mythicMob.getEntityType()));

        this.mythicMobsSupport = mythicMobsSupport;
        this.mythicMob = mythicMob;

        //TODO deprecated stuff
        EntityType entityType = EntityType.fromName(mythicMob.getEntityType());
        if (entityType == null) {
            entityType = EntityType.valueOf(mythicMob.getEntityType().toUpperCase());
        }
        if (entityType != null && LivingEntity.class.isAssignableFrom(entityType.getEntityClass())) {
            isLivingEntity = true;
        } else {
            isLivingEntity = false;
            LogHelper.warn(mythicMob.getInternalName() + " is not a living entity, currently not compatible with Mob Arena");
        }
    }

    @Override
    public LivingEntity spawn(Arena arena, World world, Location location) {
        try {
            Entity mMob = MythicMobs.inst().getAPIHelper().spawnMythicMob(mythicMob, location, 0);
            if (mMob instanceof LivingEntity) {
                mythicMobsSupport.arenaSpawnMythicMob(arena, mMob);
                return (LivingEntity)mMob;
            } else {
                LogHelper.error(mythicMob.getInternalName() + " is not a living entity, cant spawn in Mob Arena");
                mMob.remove();
                return null;
            }
        } catch (InvalidMobTypeException e) {
            //mythic mobs were reload but ma creatures can not be unregistered for compatible
            return null;
        }
    }
}
