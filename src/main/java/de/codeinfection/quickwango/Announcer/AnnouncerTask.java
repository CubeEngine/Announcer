package de.codeinfection.quickwango.Announcer;

import de.codeinfection.quickwango.Announcer.Exceptions.AnnouncementException;
import de.codeinfection.quickwango.Announcer.Exceptions.NoAnnouncementsException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author CodeInfection
 */
public class AnnouncerTask implements Runnable
{
    private final Announcer plugin;
    private final Server server;
    private final BukkitScheduler scheduler;
    private int taskId;
    private int delay;
    private long lastExecuted;
    private boolean running;

    
    protected ArrayList<List<String>> announcements;
    protected int current;

    public AnnouncerTask(Announcer plugin) throws NoAnnouncementsException
    {
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.scheduler = this.server.getScheduler();
        this.taskId = -1;
        this.delay = 0;
        this.lastExecuted = 0;
        this.running = false;

        if (!this.plugin.config.instantStart)
        {
            this.delay = this.plugin.config.interval;
        }

        this.current = 0;
        this.announcements = new ArrayList<List<String>>();
        for (String name : this.plugin.config.announcements)
        {
            try
            {
                this.announcements.add(Announcer.loadAnnouncement(name));
                Announcer.debug("Loaded '" + name + "'!");
            }
            catch (AnnouncementException e)
            {
                Announcer.error("Failed to load the announcement '" + name + "'!");
                Announcer.error("Caused by: " + (e.getCause() != null ? e.getCause().getLocalizedMessage() : e.getClass().getSimpleName()));
            }
        }
        if (this.announcements.isEmpty())
        {
            throw new NoAnnouncementsException();
        }
        Announcer.log("Loaded " + this.announcements.size() + " message files!");
    }

    public boolean start()
    {
        if (this.plugin.getServer().getOnlinePlayers().length > 0)
        {
            this.delay = Math.max(0, this.delay);
            this.taskId = this.scheduler.scheduleAsyncRepeatingTask(this.plugin, this, this.delay, this.plugin.config.interval);
            if (this.taskId < 0)
            {
                this.running = false;
                return false;
            }
            this.running = true;
        }
        return true;
    }

    public boolean isRunning()
    {
        return this.running;
    }

    public boolean stop()
    {
        return this.stop(false);
    }

    public boolean stop(boolean clearDelay)
    {
        if (this.plugin.config.instantStart)
        {
            this.delay = 0;
        }
        else
        {
            if (clearDelay)
            {
                this.delay = this.plugin.config.interval;
            }
            else
            {
                this.delay = Math.round((int)(System.currentTimeMillis() - this.lastExecuted) / 1000 * 20);
            }
        }
        
        if (this.taskId > -1)
        {
            this.scheduler.cancelTask(this.taskId);
            this.running = false;
            return true;
        }
        return false;
    }

    private List<String> nextAnnouncement()
    {
        List<String> announcement = this.announcements.get(current);
        ++this.current;
        if (current >= this.announcements.size())
        {
            this.current = 0;
        }
        return announcement;
    }

    public void run()
    {
        this.lastExecuted = System.currentTimeMillis();
        for (String line : this.nextAnnouncement())
        {
            this.server.broadcastMessage(line);
        }
    }
}
