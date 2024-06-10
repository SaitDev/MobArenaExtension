package me.sait.mobarena.core.api;

public interface Extension {
    boolean shouldEnable();

    void onEnable();

    void onDisable();
}
