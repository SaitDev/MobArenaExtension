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
        unregisterListeners();
        unregisterMobs();
        if (!shouldEnable()) {
            LogHelper.info("Disabled extension MythicMobs, no mythic mobs will work in MobArena");
            mobArenaAdapter.coreReload();
        }
    }

    public String getMythicMobsVersion() {
        if (!Bukkit.getServer().getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
            LogHelper.warn("MythicMobs plugin is not enabled");
            return null;
        }
        return MythicBukkit.inst().getVersion();
    }

    public void reload() {
        extension.reloadExtension(this);
    }

    private void registerListeners() {
        val mythicMobListener = new MythicMobListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(mythicMobListener, extension);
        listeners.add(mythicMobListener);

        val mobArenaMythicMobListener = new MobArenaMMListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(mobArenaMythicMobListener, extension);
        listeners.add(mobArenaMythicMobListener);
    }

    private void unregisterListeners() {
        for (Listener listener : listeners.toArray(new Listener[0])) {
            HandlerList.unregisterAll(listener);
            listeners.remove(listener);
        }
    }

    private void registerMobs() {
        Collection<MythicMob> mmMobs = MythicBukkit.inst().getMobManager().getMobTypes();
        for (MythicMob mob : mmMobs) {
            RuntimeException lastEx = null;
            try {
                registerMob(mob);
            } catch (RuntimeException e) {
                LogHelper.error("Unable to load mob {0} for MythicMob extension", mob.getInternalName());
                lastEx = e;
            }
            if (lastEx != null) throw lastEx;
        }
    }

    private void registerMob(MythicMob mob) {
        //cache
        String creatureKey = MythicMobCreature.getCreatureKey(mob);
        MACreature existedCreature = MACreature.fromString(creatureKey);
        if (existedCreature != null) {
            if (existedCreature instanceof MythicMobCreature) {
                LogHelper.error("Can not register mythic mobs with similar name: {0}, {1}",
                        mob.getInternalName(),
                        ((MythicMobCreature)existedCreature).getMythicMob().getInternalName());
                //unregister both to avoid confused/misused
                unregisterMob(((MythicMobCreature) existedCreature).getMythicMob());
            } else {
                originalMACreatures.put(creatureKey, existedCreature);
            }
        }

        //register
        MythicMobCreature mmCreature = new MythicMobCreature(this, mob);
        if (mmCreature.isLivingEntity()) {
            MythicMobCreature.register(creatureKey, mmCreature);
            registeredMobs.add(mob);
            LogHelper.debug("Registered mythic mob: {0}", mob.getInternalName());
        }
    }

    private void unregisterMobs() {
        for (MythicMob registeredMob : registeredMobs.toArray(new MythicMob[0])) {
            unregisterMob(registeredMob);
        }
    }

    private void unregisterMob(MythicMob registeredMob) {
        String creatureKey = MythicMobCreature.getCreatureKey(registeredMob);
        MACreature originalMACreature = originalMACreatures.get(creatureKey);
        if (originalMACreature == null || originalMACreature instanceof MythicMobCreature) {
            MythicMobCreature.register(creatureKey, null);
        } else {
            MythicMobCreature.register(creatureKey, originalMACreature);
        }
        registeredMobs.remove(registeredMob);
    }

}
