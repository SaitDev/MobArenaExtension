package me.sait.mobarena.extension.commands;

import lombok.RequiredArgsConstructor;
import me.sait.mobarena.extension.MobArenaExtension;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class CommandHandler implements CommandExecutor {
    private final MobArenaExtension extension;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //simple command for now. TODO improve it later
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                extension.reload();
                commandSender.sendMessage("[MobArenaExtension] finish reloading");
            }
        }
        return true;
    }
}
