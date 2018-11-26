package me.sait.mobarena.extension.integration.proxy;

import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.integration.proxy.listeners.MobArenaListener;
import org.bukkit.Bukkit;

public class BungeeCordSupport {
    private MobArenaExtension extension;

    public BungeeCordSupport(MobArenaExtension extension) {
        this.extension = extension;
    }

    public void registerListeners() {
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this.extension, "MobArena");
        Bukkit.getServer().getPluginManager().registerEvents(new MobArenaListener(this.extension), this.extension);
    }
}
