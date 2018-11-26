package me.sait.mobarena.extension.bungee.listeners;

import java.time.LocalDateTime;
import me.sait.mobarena.extension.bungee.MobArenaBungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {
    private MobArenaBungeeCord pluginBungee;

    public ChatListener(MobArenaBungeeCord mobArenaBungeeCord) {
        this.pluginBungee = mobArenaBungeeCord;
    }

    @EventHandler
    public void onPlayerChat(ChatEvent event) {
        if (event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer)event.getSender();
            System.out.println(event.getMessage());
            System.out.println(LocalDateTime.now().toString());
            if (this.pluginBungee.getPlayerManager().isIsolatedChat(player)) {
                event.setCancelled(true);
            }
        }

    }
}
