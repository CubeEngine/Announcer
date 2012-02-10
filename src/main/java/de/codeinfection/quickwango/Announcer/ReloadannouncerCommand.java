package de.codeinfection.quickwango.Announcer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

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
        if (sender instanceof Permissible && !((Permissible)sender).hasPermission("Announcer.reload"))
        {
            sender.sendMessage(ChatColor.RED + "Permission denied!");
            return true;
        }

        this.plugin.onDisable();
        this.plugin.onEnable();
        sender.sendMessage(ChatColor.GREEN + "Announcer was successfully reloaded!");

        return true;
    }
}
