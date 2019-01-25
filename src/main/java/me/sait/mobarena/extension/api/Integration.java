package me.sait.mobarena.extension.api;

public interface Integration {
    void onEnable();

    void onReload();

    void onDisable();
}
