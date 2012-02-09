package de.codeinfection.quickwango.Announcer;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 *
 * @author CodeInfection
 */
public class AnnouncerConfiguration
{
    public final int interval;
    public final boolean instantStart;
    public final boolean debug;
    public final String directory;
    public final List<String> announcements;

    public AnnouncerConfiguration(Configuration config) throws InvalidConfigurationException
    {
        this.instantStart = config.getBoolean("instantStart");
        this.debug = config.getBoolean("debug");
        this.directory = config.getString("directory");
        this.announcements = config.getStringList("announcements");

        Pattern pattern = Pattern.compile("^(\\d+)([tsmhd])?$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(config.getString("interval"));
        matcher.find();
        int tmp = 0;
        try
        {
            tmp = Integer.valueOf(String.valueOf(matcher.group(1)));
        }
        catch (NumberFormatException e)
        {
            throw new InvalidConfigurationException("The given interval was invalid!");
        }
        String unitSuffix = matcher.group(2);
        if (unitSuffix == null)
        {
            unitSuffix = "m";
        }
        switch (unitSuffix.toLowerCase().charAt(0))
        {
            case 'd':
                tmp *= 24;
            case 'h':
                tmp *= 60;
            case 'm':
                tmp *= 60;
            case 's':
                tmp *= 20;
        }
        this.interval = tmp;
    }
}
