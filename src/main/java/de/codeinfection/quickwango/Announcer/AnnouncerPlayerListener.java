package de.codeinfection.quickwango.Announcer;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author CodeInfection
 */
public class AnnouncerPlayerListener extends PlayerListener
{
    private final Announcer plugin;
    private final AnnouncerTask task;

    public AnnouncerPlayerListener(Announcer plugin, AnnouncerTask task)
    {
        this.plugin = plugin;
        this.task = task;
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (!this.task.isRunning())
        {
            this.task.start();
        }
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (this.plugin.getServer().getOnlinePlayers().length - 1 <= 0)
        {
            this.task.stop();
        }
    }
}
