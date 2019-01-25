package me.sait.mobarena.extension.integration.placeholderapi.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MAPlaceholderEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String param;
    private String result;

    public MAPlaceholderEvent(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
