package de.codeinfection.quickwango.Announcer;

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
    protected ArrayList<List<String>> announcenements;
    protected int current;
    protected Server server;

    public AnnouncerTask(final Server server, final File dataFolder) throws AnnouncementLoadException
    {
        this.current = 0;
        this.server = server;
        this.announcenements = new ArrayList<List<String>>();
        File announcementDir = new File(dataFolder, "scheduledMessages");
        announcementDir.mkdirs();
        File[] announcementFiles = announcementDir.listFiles(new TxtFilter());
        for (File announcementFile : announcementFiles)
        {
            try
            {
                this.announcenements.add(Announcer.loadAnnouncement(announcementFile));
                Announcer.debug("Loaded " + announcementFile.getName() + "!");
            }
            catch (AnnouncementLoadException e)
            {
                Announcer.error("Failed to load the announcement '" + announcementFile.getName() + "'!");
                Announcer.error("\t>" + e.getMessage());
            }
        }
        if (this.announcenements.isEmpty())
        {
            throw new AnnouncementLoadException("No announcement found!", 1);
        }
        Announcer.log("Loaded " + this.announcenements.size() + " message files!");
    }

    protected List<String> nextAnnouncement()
    {
        List<String> announcement = this.announcenements.get(current);
        ++this.current;
        if (current >= this.announcenements.size())
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
