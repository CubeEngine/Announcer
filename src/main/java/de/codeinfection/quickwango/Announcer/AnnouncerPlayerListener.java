package de.codeinfection.quickwango.Announcer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author CodeInfection
 */
public class AnnouncerPlayerListener implements Listener
{
    private final Announcer plugin;
    private final AnnouncerTask task;

    public AnnouncerPlayerListener(Announcer plugin, AnnouncerTask task)
    {
        this.plugin = plugin;
        this.task = task;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (!this.task.isRunning())
        {
            this.task.start();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (this.plugin.getServer().getOnlinePlayers().length - 1 <= 0)
        {
            this.task.stop();
        }
    }
}
