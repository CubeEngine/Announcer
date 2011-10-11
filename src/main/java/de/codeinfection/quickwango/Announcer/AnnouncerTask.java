package de.codeinfection.quickwango.Announcer;

import de.codeinfection.quickwango.Announcer.Exceptions.AnnouncementException;
import de.codeinfection.quickwango.Announcer.Exceptions.NoAnnouncementsException;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Server;

/**
 *
 * @author CodeInfection
 */
public class AnnouncerTask implements Runnable
{
    protected ArrayList<List<String>> announcements;
    protected int current;
    protected Server server;

    public AnnouncerTask(final Server server, final List<String> announcementNames) throws NoAnnouncementsException
    {
        this.current = 0;
        this.server = server;
        this.announcements = new ArrayList<List<String>>();
        for (String name : announcementNames)
        {
            try
            {
                this.announcements.add(Announcer.loadAnnouncement(name));
                Announcer.debug("Loaded '" + name + "'!");
            }
            catch (AnnouncementException e)
            {
                Announcer.error("Failed to load the announcement '" + name + "'!");
                Announcer.error("\t>" + e.getCause() != null ? e.getCause().getLocalizedMessage() : e.getClass().getSimpleName());
            }
        }
        if (this.announcements.isEmpty())
        {
            throw new NoAnnouncementsException();
        }
        Announcer.log("Loaded " + this.announcements.size() + " message files!");
    }

    protected List<String> nextAnnouncement()
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
        for (String line : this.nextAnnouncement())
        {
            this.server.broadcastMessage(line);
        }
    }

    private class TxtFilter implements FileFilter
    {
        public boolean accept(File file)
        {
            return (!file.isDirectory() && file.getName().endsWith(".txt"));
        }
    }
}
