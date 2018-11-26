package me.sait.mobarena.extension.integration.placeholderapi;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.config.Constants;
import me.sait.mobarena.extension.utils.CommonUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlaceholderAPISupport extends PlaceholderExpansion {
    public static final String pluginName = "PlaceholderAPI";
    private static final String identifier = "mobarena";
    private MobArenaExtension extension;
    private MobArena mobArena;

    public PlaceholderAPISupport(MobArenaExtension extension, MobArena mobArena) {
        this.extension = extension;
        this.mobArena = mobArena;
    }

    @Override
    public String onPlaceholderRequest(Player player, String param) {
        //global
        if (param.equalsIgnoreCase("prefix")) {
            return getGlobalPrefix();
        } else if (param.equalsIgnoreCase("total_enabled")) {
            return String.valueOf(mobArena.getArenaMaster().getEnabledArenas().size());
        }

        //player specific
        if (player == null) return "";

        Arena arena = mobArena.getArenaMaster().getArenaWithPlayer(player);
        if (arena == null) {
            mobArena.getArenaMaster().getArenaWithSpectator(player);
        }

        if (param.toLowerCase().startsWith("arena")) {
            if (arena == null) return "";

            if (param.equalsIgnoreCase("arena") || param.equalsIgnoreCase("arena_name")) {
                return arena.getSettings().getName();
            } else if (param.equalsIgnoreCase("arena_prefix")) {
                String prefix = arena.getSettings().getString("prefix", "");
                if (prefix.isEmpty()) {
                    return getGlobalPrefix();
                } else {
                    return prefix;
                }
            }
        } else if (param.toLowerCase().startsWith("player")) {
            ;
        }

        return null;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getAuthor() {
        String authors = "";
        if (!CommonUtils.isEmptyList(extension.getDescription().getAuthors())) {
            if (extension.getDescription().getAuthors().size() == 1) {
                authors = extension.getDescription().getAuthors().get(0);
            } else {
                authors += "[";
                authors += String.join(", ", extension.getDescription().getAuthors());
                authors += "]";
            }
        }
        return authors;
    }

    @Override
    public String getVersion() {
        return extension.getDescription().getVersion();
    }

    private String getGlobalPrefix() {
        String prefix = mobArena.getConfig().getString("global-settings.prefix", "");
        if (prefix.isEmpty()) {
            prefix = ChatColor.GREEN + "[" + Constants.MOB_ARENA_PLUGIN_NAME + "] ";
        }
        return prefix;
    }
}
