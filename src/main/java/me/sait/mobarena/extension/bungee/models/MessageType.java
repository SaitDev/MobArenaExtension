package me.sait.mobarena.extension.bungee.models;

public enum MessageType {
    EVENT_ARENA_JOIN("Arena Join"),
    EVENT_ARENA_LEAVE("Arena Leave"),
    EVENT_ARENA_START("Arena Start"),
    EVENT_ARENA_END("Arena End");

    private String value;

    private MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static MessageType findByValue(String value) {
        if (value == null) {
            return null;
        } else {
            MessageType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                MessageType type = var1[var3];
                if (type.getValue().equals(value)) {
                    return type;
                }
            }

            return null;
        }
    }
}
