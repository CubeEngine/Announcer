package de.codeinfection.quickwango.Announcer;

import de.codeinfection.quickwango.Announcer.Exceptions.AnnouncementException;
import de.codeinfection.quickwango.Announcer.Exceptions.AnnouncementNotFoundException;
import de.codeinfection.quickwango.Announcer.Exceptions.InvalidAnnouncementNameException;
import de.codeinfection.quickwango.Announcer.Exceptions.NoAnnouncementsException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.scheduler.BukkitScheduler;

public class Announcer extends JavaPlugin
{
    protected static final Logger logger = Logger.getLogger("Minecraft");
    public static boolean debugMode = false;
    public static File announcementDir = null;
    private AnnouncerTask task;
    
    protected Server server;
    protected PluginManager pm;
    protected AnnouncerConfiguration config;
    protected BukkitScheduler scheduler;

    public void onEnable()
    {
        this.server = this.getServer();
        this.pm = this.server.getPluginManager();
        this.scheduler = this.server.getScheduler();

        Configuration configFile = this.getConfig();
        configFile.options().copyDefaults(true);
        configFile.addDefault("directory", this.getDataFolder().getPath() + File.separator + "announcements");
        try
        {
            this.config = new AnnouncerConfiguration(configFile);
        }
        catch (InvalidConfigurationException e)
        {
            error("Failed to load the configuration due to an invalid value!", e);
            error("Staying in a zombie state...");
            return;
        }

        debugMode = this.config.debug;
        announcementDir = new File(this.config.directory);
        announcementDir.mkdirs();

        debug("Calculated a interval of " + this.config.interval + " ticks");

        this.getCommand("announce").setExecutor(new AnnounceCommand(this.server));
        this.getCommand("reloadannouncer").setExecutor(new ReloadannouncerCommand(this));

        try
        {
            this.task = new AnnouncerTask(this);
            if (task.start())
            {
                AnnouncerPlayerListener playerListener = new AnnouncerPlayerListener(this, this.task);
                this.pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Monitor, this);
                this.pm.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Monitor, this);
            }
            else
            {
                error("Failed to schedule the announcer task!");
            }
        }
        catch (NoAnnouncementsException e)
        {
            error("No announcements found!");
        }

        this.saveConfig();

        log("Version " + this.getDescription().getVersion() + " enabled");
    }

    public void onDisable()
    {
        this.task.stop(true);
        log("Version " + this.getDescription().getVersion() + " disabled");
    }

    public static void log(Level logLevel, String msg, Throwable t)
    {
        logger.log(logLevel, "[Announcer] " + msg, t);
    }

    public static void log(Level logLevel, String msg)
    {
        log(logLevel, msg, null);
    }

    public static void log(String msg)
    {
        log(Level.INFO, msg);
    }

    public static void error(String msg)
    {
        log(Level.SEVERE, msg);
    }

    public static void error(String msg, Throwable t)
    {
        log(Level.SEVERE, msg, t);
    }

    public static void debug(String msg)
    {
        if (debugMode)
        {
            log("[debug] " + msg);
        }
    }

    public static List<String> loadAnnouncement(String name) throws AnnouncementException
    {
        if (!name.matches("[\\.\\\\/\\*\\?:\\|<>\"]"))
        {
            return loadAnnouncement(new File(announcementDir, name + ".txt"));
        }
        else
        {
            throw new InvalidAnnouncementNameException();
        }
    }

    public static List<String> loadAnnouncement(File file) throws AnnouncementException
    {
        List<String> lines = new ArrayList<String>();
        if (!file.exists())
        {
            throw new AnnouncementNotFoundException();
        }
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                lines.add(line.trim().replaceAll("&([a-f0-9])", "\u00A7$1"));
            }
            reader.close();
        }
        catch (IOException e)
        {
            throw new AnnouncementException("IOException: " + e.getLocalizedMessage(), e);
        }
        return lines;
    }
}
