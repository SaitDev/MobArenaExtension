package me.sait.mobarena.extension.integration.discordsrv;

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

        if (discordSrvSupport.getMobArenaAdapter().inIsolatedChatArena(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
