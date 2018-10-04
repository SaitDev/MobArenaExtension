package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.waves.MACreature;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.entity.EntityType;

import java.util.Collection;

public class MythicMobsSupport {
    MobArena mobArena;

    public MythicMobsSupport(MobArena mobArena) {
        this.mobArena = mobArena;
    }

    public void registerMobs() {
        Collection<MythicMob> mmMobs = MythicMobs.inst().getMobManager().getMobTypes();
        System.out.println(mmMobs.size());
//        System.out.println(MythicMobs.inst().getMobManager().get);
        for (MythicMob mob : mmMobs) {
//            System.out.println(mob.getDisplayName());
//            System.out.println(mob.getFileName());
//            System.out.println(mob.getInternalName());
//            System.out.println(mob.getEntityType());
//            System.out.println(mob.getMythicEntity());
            new MythicMobCreature(mob);
        }
    }
}
