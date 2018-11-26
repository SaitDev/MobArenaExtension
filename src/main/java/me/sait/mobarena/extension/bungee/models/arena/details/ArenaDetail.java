package me.sait.mobarena.extension.bungee.models.arena.details;

public class ArenaDetail {
    private String arenaName;
    private String configName;
    private Boolean isolatedChat;
    private Integer lobbyPlayers;
    private Integer specPlayers;
    private Integer arenaPlayers;

    public ArenaDetail() {
    }

    public String getArenaName() {
        return this.arenaName;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public String getConfigName() {
        return this.configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Boolean isIsolatedChat() {
        return this.isolatedChat;
    }

    public void setIsolatedChat(Boolean isolatedChat) {
        this.isolatedChat = isolatedChat;
    }

    public Integer getLobbyPlayers() {
        return this.lobbyPlayers;
    }

    public void setLobbyPlayers(Integer lobbyPlayers) {
        this.lobbyPlayers = lobbyPlayers;
    }

    public Integer getSpecPlayers() {
        return this.specPlayers;
    }

    public void setSpecPlayers(Integer specPlayers) {
        this.specPlayers = specPlayers;
    }

    public Integer getArenaPlayers() {
        return this.arenaPlayers;
    }

    public void setArenaPlayers(Integer arenaPlayers) {
        this.arenaPlayers = arenaPlayers;
    }
}
