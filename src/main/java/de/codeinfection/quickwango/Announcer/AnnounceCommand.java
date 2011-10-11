package de.codeinfection.quickwango.Announcer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class AnnounceCommand implements CommandExecutor
{
    protected Server server;
    protected File announcementDir;

    public AnnounceCommand(Server server, File dataFolder)
    {
        this.server = server;
        this.announcementDir = new File(dataFolder, "manualMessages");
        this.announcementDir.mkdirs();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (!((Player)sender).hasPermission("Announcer.announce"))
            {
                sender.sendMessage(ChatColor.RED + "Permission denied!");
                return true;
            }
        }
        if (args.length > 0)
        {
            if (!args[0].matches("[\\.\\\\/\\*\\?:\\|<>\"]"))
            {
                File announcementFile = new File(this.announcementDir, args[0] + ".txt");
                try
                {
                    for (String line : Announcer.loadAnnouncement(announcementFile))
                    {
                        this.server.broadcastMessage(line);
                    }
                }
                catch (AnnouncementLoadException e)
                {
                    switch (e.id)
                    {
                        case 1:
                            sender.sendMessage(ChatColor.RED + "The requested announcement is not available!");
                            break;
                        case 2:
                            sender.sendMessage(ChatColor.RED + "Failed to load the announcement!");
                            Announcer.error("Failed to load the announcement '" + announcementFile.getName() + "'!");
                            Announcer.error("\t>" + e.getMessage());
                            break;
                    }
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "The given name is invalid!");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "No file name given!");
            return false;
        }
        return true;
    }
}
