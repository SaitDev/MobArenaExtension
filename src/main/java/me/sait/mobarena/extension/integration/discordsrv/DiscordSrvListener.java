package me.sait.mobarena.extension.integration.discordsrv;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;

public class DiscordSrvListener {
    public DiscordSrvAdapter discordSrvAdapter;

    public DiscordSrvListener(DiscordSrvAdapter discordSrvAdapter) {
        this.discordSrvAdapter = discordSrvAdapter;
    }

    @Subscribe
    public void onMinecraftMessagePreProcess(GameChatMessagePreProcessEvent event) {
        if (event.isCancelled()) return;

        if (discordSrvAdapter.getMobArenaAdapter().inIsolatedChatArena(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
