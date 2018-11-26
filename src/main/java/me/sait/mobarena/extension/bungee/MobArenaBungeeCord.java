package me.sait.mobarena.extension.bungee;

import me.sait.mobarena.extension.bungee.listeners.ChannelMessageListener;
import me.sait.mobarena.extension.bungee.listeners.ChatListener;
import me.sait.mobarena.extension.bungee.managers.PlayerManager;
import net.md_5.bungee.api.plugin.Plugin;

public class MobArenaBungeeCord extends Plugin {
    private PlayerManager playerManager;

    public MobArenaBungeeCord() {
    }

    public void onEnable() {
        this.playerManager = new PlayerManager();
        this.registerListeners();
    }

    public void onDisable() {
    }

    private void registerListeners() {
        this.getProxy().registerChannel("MobArena");
        this.getProxy().getPluginManager().registerListener(this, new ChannelMessageListener(this));
        this.getProxy().getPluginManager().registerListener(this, new ChatListener(this));
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
}
