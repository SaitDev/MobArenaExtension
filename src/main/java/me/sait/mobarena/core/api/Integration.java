package me.sait.mobarena.core.api;

public interface Integration {
    boolean shouldEnable();

    void onEnable();

    void onDisable();
}
