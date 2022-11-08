package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.waves.MACreature;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.sait.mobarena.core.MobArenaAdapter;
import me.sait.mobarena.core.api.Integration;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.config.ConfigManager;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RequiredArgsConstructor
public class MythicMobsAdapter implements Integration {
    public static final String PLUGIN_NAME = "MythicMobs";
    private final MobArenaExtension extension;
    @Getter(AccessLevel.PACKAGE)
    private final MobArenaAdapter mobArenaAdapter;
    @Getter(AccessLevel.PACKAGE)
    private MythicMobEntityManager mythicMobEntityManager = new MythicMobEntityManager(this);
    private final Set<Listener> listeners = new HashSet<>();
    private final Map<String, MACreature> originalMACreatures = new HashMap<>();
    private final List<MythicMob> registeredMobs = new ArrayList();

    @Override
    public boolean shouldEnable() {
        return ConfigManager.isMythicMobEnabled();
    }

    @Override
    public void onEnable() {
        if (!Bukkit.getServer().getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
            LogHelper.log(
                    "MythicMobs plugin can not be found. Install it or disable mythicmob extension in config",
                    LogLevel.CRITICAL
            );
//                getServer().getPluginManager().disablePlugin(this);
        }

        registerMobs();
        registerListeners();

        mobArenaAdapter.reloadIfFail();
    }

    @Override
    public void onDisable() {
    }

    private void registerListeners() {
        val mythicMobListener = new MythicMobListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(mythicMobListener, extension);
        listeners.add(mythicMobListener);

        val mobArenaMythicMobListener = new MobArenaMythicMobListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(mobArenaMythicMobListener, extension);
        listeners.add(mobArenaMythicMobListener);
    }

    private void registerMobs() {
        Collection<MythicMob> mmMobs = MythicBukkit.inst().getMobManager().getMobTypes();
        for (MythicMob mob : mmMobs) {
            String creatureKey = MythicMobCreature.getCreatureKey(mob);
            MACreature existedCreature = MACreature.fromString(creatureKey);
            if (existedCreature != null) {
                if (existedCreature instanceof MythicMobCreature) {
                    throw new IllegalArgumentException("Can not register mythic mobs with similar name: " + mob.getInternalName());
                } else {
                    originalMACreatures.put(creatureKey, existedCreature);
                }
            }

            MythicMobCreature mmCreature = new MythicMobCreature(this, mob);
            if (mmCreature.isLivingEntity()) {
                MythicMobCreature.register(creatureKey, mmCreature);
                registeredMobs.add(mob);
                LogHelper.debug("Registered mythic mob: " + mob.getInternalName());
            }
        }
    }

}
