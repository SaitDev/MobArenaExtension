package me.sait.mobarena.extension.bungee.managers;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerManager {
    List<ProxiedPlayer> isolatedChatPlayers = new ArrayList();

    public PlayerManager() {
    }

    public void playerJoinArena(ProxiedPlayer player, boolean isolatedChat) {
        if (isolatedChat && !this.isolatedChatPlayers.contains(player)) {
            this.isolatedChatPlayers.add(player);
        }

    }

    public void playerLeaveArena(ProxiedPlayer player) {
        if (this.isolatedChatPlayers.contains(player)) {
            this.isolatedChatPlayers.remove(player);
        }

    }

    public boolean isIsolatedChat(ProxiedPlayer player) {
        return this.isolatedChatPlayers.contains(player);
    }
}
