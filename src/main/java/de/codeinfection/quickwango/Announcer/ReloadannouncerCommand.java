package de.codeinfection.quickwango.Announcer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author CodeInfection
 */
public class ReloadannouncerCommand implements CommandExecutor
{
    protected Announcer plugin;

    public ReloadannouncerCommand(Announcer plugin)
    {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender.hasPermission("Announcer.reload"))
        {
            this.plugin.onDisable();
            this.plugin.onEnable();
            sender.sendMessage(ChatColor.GREEN + "Announcer was successfully reloaded!");
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "Permission denied!");
        }

        return true;
    }
}
