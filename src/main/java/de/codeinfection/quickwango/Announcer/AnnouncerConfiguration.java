package de.codeinfection.quickwango.Announcer;

import java.util.List;
import org.bukkit.configuration.Configuration;

/**
 *
 * @author CodeInfection
 */
public class AnnouncerConfiguration
{
    public final String interval;
    public final boolean instantStart;
    public final boolean debug;
    public final String directory;
    public final List<String> announcements;

    public AnnouncerConfiguration(Configuration config)
    {
        this.interval = config.getString("interval");
        this.instantStart = config.getBoolean("instantStart");
        this.debug = config.getBoolean("debug");
        this.directory = config.getString("directory");
        this.announcements = (List<String>)config.getList("announcements");
    }
}
