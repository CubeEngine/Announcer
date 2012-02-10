package de.codeinfection.quickwango.Announcer;

import de.codeinfection.quickwango.Announcer.Exceptions.AnnouncementException;
import de.codeinfection.quickwango.Announcer.Exceptions.AnnouncementNotFoundException;
import de.codeinfection.quickwango.Announcer.Exceptions.InvalidAnnouncementNameException;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

/**
 *
 * @author CodeInfection
 */
public class AnnounceCommand implements CommandExecutor
{
    protected Server server;

    public AnnounceCommand(Server server)
    {
        this.server = server;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Permissible && !((Permissible)sender).hasPermission("Announcer.announce"))
        {
            sender.sendMessage(ChatColor.RED + "Permission denied!");
            return true;
        }
        if (args.length > 0)
        {
            try
            {
                for (String line : Announcer.loadAnnouncement(args[0]))
                {
                    this.server.broadcastMessage(line);
                }
            }
            catch (AnnouncementNotFoundException e)
            {
                sender.sendMessage(ChatColor.RED + "The requested announcement is not available!");
            }
            catch (InvalidAnnouncementNameException e)
            {
                sender.sendMessage(ChatColor.RED + "The requested announcement has a invalid name!");
            }
            catch (AnnouncementException e)
            {
                sender.sendMessage(ChatColor.RED + "Failed to load the announcement!");
                Throwable cause = e.getCause();
                if (cause != null)
                {
                    Announcer.error(cause.getLocalizedMessage(), cause);
                }
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
