package de.codeinfection.quickwango.Anouncer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class ReloadanouncerCommand implements CommandExecutor
{
    protected Anouncer plugin;

    public ReloadanouncerCommand(Anouncer plugin)
    {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player && !Anouncer.has((Player)sender, "Anouncer.reload"))
        {
            sender.sendMessage(ChatColor.RED + "Permission denied!");
            return true;
        }

        this.plugin.onDisable();
        this.plugin.onEnable();
        sender.sendMessage(ChatColor.GREEN + "Anouncer was successfully reloaded!");

        return true;
    }
}
