package me.sait.mobarena.extension.integration.discordsrv;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import github.scarsz.discordsrv.DiscordSRV;
import me.sait.mobarena.extension.integration.Integration;
import me.sait.mobarena.extension.integration.discordsrv.listeners.DiscordSrvListener;
import org.bukkit.entity.Player;

public class DiscordSrvSupport implements Integration {
    public static final String pluginName = "DiscordSRV";
    private MobArena mobArena;
    private DiscordSrvListener discordSrvListener;

    public DiscordSrvSupport(MobArena mobArena) {
        this.mobArena = mobArena;
    }

    @Override
    public void onEnable() {
        registerListeners();
    }

    @Override
    public void onDisable() {
        unregisterListeners();
    }

    public boolean inIsolatedChatArena(Player player) {
        Arena arena = mobArena.getArenaMaster().getArenaWithPlayer(player);
        if (arena != null && arena.hasIsolatedChat()) {
            return true;
        }
        return false;
    }

    private void registerListeners() {
        if (discordSrvListener == null) {
            discordSrvListener = new DiscordSrvListener(this);
        }
        DiscordSRV.api.subscribe(discordSrvListener);
    }

    private void unregisterListeners() {
        if (discordSrvListener != null) {
            DiscordSRV.api.unsubscribe(discordSrvListener);
            discordSrvListener = null;
        }
    }
}
