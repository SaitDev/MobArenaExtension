package me.sait.mobarena.extension.integration.placeholderapi;

import com.garbagemule.MobArena.ArenaPlayer;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import lombok.val;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.api.Integration;
import me.sait.mobarena.extension.config.Constants;
import me.sait.mobarena.extension.integration.placeholderapi.events.MAPlaceholderEvent;
import me.sait.mobarena.extension.log.LogHelper;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class PlaceholderAPISupport extends PlaceholderExpansion implements Integration {
    public static final String pluginName = "PlaceholderAPI";
    private static final String identifier = "mobarena";
    private final MobArenaExtension extension;
    private final MobArena mobArena;

    public PlaceholderAPISupport(MobArenaExtension extension, MobArena mobArena) {
        this.extension = extension;
        this.mobArena = mobArena;
    }

    @Override
    public String onRequest(OfflinePlayer player, String param) {
        MAPlaceholderEvent event = new MAPlaceholderEvent(param);
        Bukkit.getPluginManager().callEvent(event);

        if (event.getResult() != null) {
            if (StringUtils.isBlank(event.getResult())) {
                LogHelper.debug("Some plugin override this placeholder with an empty value: " + param);
            }
            return event.getResult();
        }

        if (param == null) return null;
        param = param.toLowerCase();

        //global
        if (param.equalsIgnoreCase("prefix")) {
            return getGlobalPrefix();
        } else if (param.equalsIgnoreCase("total_enabled")) {
            return String.valueOf(mobArena.getArenaMaster().getEnabledArenas().size());
        }

        //player specific
        if (player == null) return "";

        Arena arena = mobArena.getArenaMaster().getArenaWithPlayer(player.getPlayer());
        if (arena == null) {
            mobArena.getArenaMaster().getArenaWithSpectator(player.getPlayer());
        }

        if (param.toLowerCase().startsWith("arena")) {
            if (arena == null) return "";
            val arenaPlayer = arena.getArenaPlayer(player.getPlayer());

            switch (param) {
                case "arena":
                case "arena_name":
                    return arena.getSettings().getName();
                case "arena_prefix":
                    String prefix = arena.getSettings().getString("prefix", "");
                    return prefix.isEmpty() ? getGlobalPrefix() : prefix;

                case "arena_wave":
                    return String.valueOf(arena.getWaveManager().getWaveNumber());
                case "arena_final_wave":
                    if (arena.getWaveManager().getFinalWave() > 0) {
                        return String.valueOf(arena.getWaveManager().getFinalWave());
                    } else {
                        return "∞";
                    }

                case "arena_mobs":
                    return String.valueOf(arena.getMonsterManager().getMonsters().size());

                //TODO those stats provided by core with keys were hard coded with name, might broken in the future
                case "arena_killed":
                    return arenaPlayer == null ? null
                            : String.valueOf(arenaPlayer.getStats().getInt("kills"));
                case "arena_damage_dealt":
                    return arenaPlayer == null ? null
                            : String.valueOf(arenaPlayer.getStats().getInt("dmgDone"));
                case "arena_damage_received":
                    return arenaPlayer == null ? null
                            : String.valueOf(arenaPlayer.getStats().getInt("dmgTaken"));

                case "arena_player_join":
                    return String.valueOf(arena.getArenaPlayerSet().size());
                case "arena_player_live":
                    return String.valueOf(arena.getPlayersInArena().size());

            }
        } else if (param.toLowerCase().startsWith("player")) {
            switch (param) {
                case "player_total_joined": //TODO
                    break;
                case "player_total_won": //TODO
                    break;
            }
        }

        return null;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getAuthor() {
        return "Sait";
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

    @Override
    public void onEnable() {
        register();
    }

    @Override
    public void onReload() { }

    @Override
    public void onDisable() { }
}
