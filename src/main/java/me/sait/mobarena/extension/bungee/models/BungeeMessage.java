package me.sait.mobarena.extension.bungee.models;

public class BungeeMessage {
    private MessageType type;
    private String data;

    public MessageType getType() {
        return this.type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
