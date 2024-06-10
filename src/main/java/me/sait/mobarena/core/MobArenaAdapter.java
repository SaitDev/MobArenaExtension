package me.sait.mobarena.core;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import lombok.RequiredArgsConstructor;
import me.sait.mobarena.core.api.Extension;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class MobArenaAdapter implements Extension {
    private final MobArena mobArena;

    @Override
    public boolean shouldEnable() {
        return true;
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    public void reloadIfFail() {
        if (mobArena.getLastFailureCause() != null) {
            LogHelper.log("Auto reload MobArena so it load mythic mobs now if have", LogLevel.DETAIL);
            coreReload();
        }
    }

    public void coreReload() {
        try {
            mobArena.reload();
        } catch (RuntimeException error) {
            mobArena.getLogger().severe(error.getMessage());
            LogHelper.warn("Fail to reload MobArena");
        }
    }

    public boolean inIsolatedChatArena(Player player) {
        Arena arena = getArenaWithPlayer(player);
        if (arena != null && arena.hasIsolatedChat()) {
            return true;
        }
        return false;
    }

    public Arena getArenaAtLocation(Location location) {
        return mobArena.getArenaMaster().getArenaAtLocation(location);
    }

    public Arena getArenaWithPlayer(Player player) {
        return mobArena.getArenaMaster().getArenaWithPlayer(player);
    }
}
