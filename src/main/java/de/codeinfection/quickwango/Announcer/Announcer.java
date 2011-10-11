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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

public class Announcer extends JavaPlugin
{
    protected static final Logger logger = Logger.getLogger("Minecraft");
    public static boolean debugMode = false;
    public static File announcementDir = null;
    
    protected Server server;
    protected PluginManager pm;
    protected FileConfiguration config;
    protected BukkitScheduler scheduler;

    public void onEnable()
    {
        this.server = this.getServer();
        this.pm = this.server.getPluginManager();
        this.config = this.getConfig();
        this.scheduler = this.server.getScheduler();

        log("Config: " + String.valueOf(this.config.getValues(true)));
        log("DefaultConfig: " + String.valueOf(this.config.getDefaults().getValues(true)));

        this.config.addDefault("directory", this.getDataFolder().getPath());
        debugMode = this.config.getBoolean("debug", debugMode);
        announcementDir = new File(this.config.getString("directory"));
        announcementDir.mkdirs();

        log("Announcementdir: " + announcementDir.getAbsolutePath());
        
        Pattern pattern = Pattern.compile("^(\\d+)([tsmhd])?$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(this.config.getString("interval"));
        matcher.find();
        int interval = 0;
        try
        {
            interval = Integer.valueOf(String.valueOf(matcher.group(1)));
        }
        catch (NumberFormatException e)
        {
            error("The given interval was invalid!", e);
            return;
        }
        String unitSuffix = matcher.group(2);
        if (unitSuffix == null)
        {
            unitSuffix = "m";
        }
        switch (unitSuffix.toLowerCase().charAt(0))
        {
            case 'd':
                interval *= 24;
            case 'h':
                interval *= 60;
            case 'm':
                interval *= 60;
            case 's':
                interval *= 20;
        }

        debug("Calculated a interval of " + interval + " ticks");

        this.getCommand("announce").setExecutor(new AnnounceCommand(this.server));
        this.getCommand("reloadannouncer").setExecutor(new ReloadannouncerCommand(this));

        try
        {
            AnnouncerTask task = new AnnouncerTask(server, (List<String>)this.config.getList("announcements"));

            if (this.scheduler.scheduleAsyncRepeatingTask(this, task, (this.config.getBoolean("instantStart") ? 0 : interval), interval) < 0)
            {
                error("Failed to schedule the announcer task!");
                return;
            }
        }
        catch (NoAnnouncementsException e)
        {
            error("No announcements found!");
            return;
        }

        this.saveConfig();

        log(this.getDescription().getName() + " (v" + this.getDescription().getVersion() + ") enabled");
    }

    public void onDisable()
    {
        this.scheduler.cancelTasks(this);
        System.out.println(this.getDescription().getName() + " Disabled");
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
