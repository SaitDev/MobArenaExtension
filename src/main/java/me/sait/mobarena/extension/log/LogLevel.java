package me.sait.mobarena.extension.log;

public enum LogLevel {
    DEBUG,
    DETAIL,
    USEFUL,
    WARNING,
    ERROR,
    CRITICAL;

    public static LogLevel getHighestPriority() {
        LogLevel[] values = LogLevel.values();
        if (values.length < 1) return null;

        return values[values.length - 1];
    }
}
