package de.codeinfection.quickwango.Announcer;

/**
 *
 * @author CodeInfection
 */
public class AnnouncementLoadException extends Exception
{
    public final int id;

    public AnnouncementLoadException(int id)
    {
        super();
        this.id = id;
    }

    public AnnouncementLoadException(String message, int id)
    {
        super(message);
        this.id = id;
    }

    public AnnouncementLoadException(Throwable cause, int id)
    {
        super(cause);
        this.id = id;
    }

    public AnnouncementLoadException(String message, Throwable cause, int id)
    {
        super(message, cause);
        this.id = id;
    }
}
