package de.codeinfection.quickwango.Announcer.Exceptions;

/**
 *
 * @author CodeInfection
 */
public class AnnouncementException extends Exception
{
    public AnnouncementException()
    {
        super();
    }

    public AnnouncementException(String message)
    {
        super(message);
    }

    public AnnouncementException(Throwable cause)
    {
        super(cause);
    }

    public AnnouncementException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
