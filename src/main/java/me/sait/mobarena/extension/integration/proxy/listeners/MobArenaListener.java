package me.sait.mobarena.extension.integration.proxy.listeners;

import com.garbagemule.MobArena.events.ArenaPlayerJoinEvent;
import com.garbagemule.MobArena.events.ArenaPlayerLeaveEvent;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.bungee.models.BungeeMessage;
import me.sait.mobarena.extension.bungee.models.MessageType;
import me.sait.mobarena.extension.bungee.models.arena.details.ArenaDetail;
import me.sait.mobarena.extension.bungee.models.player.PlayerJoinArenaEvent;
import me.sait.mobarena.extension.config.Constants;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MobArenaListener implements Listener, Constants {
    private MobArenaExtension extension;
    private Gson gson;

    public MobArenaListener(MobArenaExtension extension) {
        this.extension = extension;
        this.gson = new Gson();
    }

    @EventHandler(ignoreCancelled = true)
    public void playerJoin(ArenaPlayerJoinEvent event) {
        ByteArrayDataOutput message = ByteStreams.newDataOutput();
        message.writeUTF("core");
        message.writeUTF("extension");
        BungeeMessage body = new BungeeMessage();
        body.setType(MessageType.EVENT_ARENA_JOIN);
        ArenaDetail detail = new ArenaDetail();
        detail.setIsolatedChat(event.getArena().hasIsolatedChat());
        detail.setConfigName(event.getArena().configName());
        PlayerJoinArenaEvent data = new PlayerJoinArenaEvent(detail);
        body.setData(this.gson.toJson(data));
        message.writeUTF(this.gson.toJson(body));
        event.getPlayer().sendPluginMessage(this.extension, "MobArena", message.toByteArray());
    }

    @EventHandler(ignoreCancelled = true)
    public void playerLeave(ArenaPlayerLeaveEvent event) {
        ByteArrayDataOutput message = ByteStreams.newDataOutput();
        message.writeUTF("core");
        message.writeUTF("extension");
        BungeeMessage body = new BungeeMessage();
        body.setType(MessageType.EVENT_ARENA_LEAVE);
        message.writeUTF(this.gson.toJson(body));
        event.getPlayer().sendPluginMessage(this.extension, "MobArena", message.toByteArray());
    }

    public void testChat(AsyncPlayerChatEvent event) {
        System.out.println(LocalDateTime.now().toString());
    }

    public void testChat2(AsyncPlayerChatEvent event) {
        System.out.println(LocalDateTime.now().toString());
    }

    public void testChat3(AsyncPlayerChatEvent event) {
        System.out.println(LocalDateTime.now().toString());
    }

    public void testChat4(AsyncPlayerChatEvent event) {
        System.out.println(LocalDateTime.now().toString());
    }
}
