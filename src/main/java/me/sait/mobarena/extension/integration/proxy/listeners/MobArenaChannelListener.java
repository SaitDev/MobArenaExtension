package me.sait.mobarena.extension.integration.proxy.listeners;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MobArenaChannelListener implements PluginMessageListener {
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        System.out.println(new String(message));
    }
}
