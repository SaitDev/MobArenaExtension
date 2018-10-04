package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.waves.MACreature;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class MythicMobCreature extends MACreature {
    private MythicMob mythicMob;

    public MythicMobCreature(MythicMob mythicMob) {
        super(mythicMob.getInternalName().toLowerCase(), EntityType.fromName(mythicMob.getEntityType()));
        this.mythicMob = mythicMob;
    }

    @Override
    public LivingEntity spawn(Arena arena, World world, Location location) {
        try {
            return (LivingEntity) MythicMobs.inst().getAPIHelper().spawnMythicMob(mythicMob, location, 0);
        } catch (InvalidMobTypeException e) { //most likely should not happen
            return null;
        }
    }
}
