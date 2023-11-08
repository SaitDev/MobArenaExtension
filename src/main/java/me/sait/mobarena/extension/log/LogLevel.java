package me.sait.mobarena.extension.log;

public enum LogLevel {
    DEBUG,
    DETAIL,
    USEFUL,
    WARNING, //some features may not work, less attention required
    ERROR, //some features won't work, need correcting
    CRITICAL; //whole module/plugin won't work

    public static LogLevel getHighestPriority() {
        LogLevel[] values = LogLevel.values();
        if (values.length < 1) return null;

        return values[values.length - 1];
    }
}
