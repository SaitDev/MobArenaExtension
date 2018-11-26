package me.sait.mobarena.extension.bungee.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import me.sait.mobarena.extension.bungee.MobArenaBungeeCord;
import me.sait.mobarena.extension.bungee.models.BungeeMessage;
import me.sait.mobarena.extension.bungee.models.MessageType;
import me.sait.mobarena.extension.bungee.models.player.PlayerJoinArenaEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChannelMessageListener implements Listener {
    private MobArenaBungeeCord pluginBungee;
    private Gson gson;

    public ChannelMessageListener(MobArenaBungeeCord mobArenaBungeeCord) {
        this.pluginBungee = mobArenaBungeeCord;
        this.gson = new Gson();
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (event.getTag().equals("MobArena")) {
            this.pluginBungee.getLogger().info(new String(event.getData()));
            ByteArrayDataInput data = ByteStreams.newDataInput(event.getData());
            String coreVersion = data.readUTF();
            String extensionVersion = data.readUTF();
            String body = data.readUTF();
            if (event.getReceiver() instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer)event.getReceiver();
                BungeeMessage message = (BungeeMessage)this.gson.fromJson(body, BungeeMessage.class);
                if (message.getType() == null) {
                    return;
                }

                if (message.getType() == MessageType.EVENT_ARENA_JOIN) {
                    PlayerJoinArenaEvent content = (PlayerJoinArenaEvent)this.gson.fromJson(message.getData(), PlayerJoinArenaEvent.class);
                    ProxyServer.getInstance().getLogger().info(content.getArena().getConfigName());
                    ProxyServer.getInstance().getLogger().info(content.getArena().isIsolatedChat().toString());
                    this.pluginBungee.getPlayerManager().playerJoinArena(player, content.getArena().isIsolatedChat());
                } else if (message.getType() == MessageType.EVENT_ARENA_LEAVE) {
                    this.pluginBungee.getPlayerManager().playerLeaveArena(player);
                }
            }
        }

    }
}
