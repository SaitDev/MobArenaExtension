package me.sait.mobarena.extension.integration.discordsrv.listeners;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import me.sait.mobarena.extension.integration.discordsrv.DiscordSrvSupport;

public class DiscordSrvListener {
    public DiscordSrvSupport discordSrvSupport;

    public DiscordSrvListener(DiscordSrvSupport discordSrvSupport) {
        this.discordSrvSupport = discordSrvSupport;
    }

    @Subscribe
    public void onMinecraftMessagePreProcess(GameChatMessagePreProcessEvent event) {
        if (event.isCancelled()) return;

        if (discordSrvSupport.inIsolatedChatArena(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
