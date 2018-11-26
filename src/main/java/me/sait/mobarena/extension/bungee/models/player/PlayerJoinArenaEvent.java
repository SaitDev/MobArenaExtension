package me.sait.mobarena.extension.bungee.models.player;

import me.sait.mobarena.extension.bungee.models.arena.details.ArenaDetail;

public class PlayerJoinArenaEvent {
    private ArenaDetail arena;

    public PlayerJoinArenaEvent() {
    }

    public PlayerJoinArenaEvent(ArenaDetail arena) {
        this.arena = arena;
    }

    public ArenaDetail getArena() {
        return this.arena;
    }

    public void setArena(ArenaDetail arena) {
        this.arena = arena;
    }
}
