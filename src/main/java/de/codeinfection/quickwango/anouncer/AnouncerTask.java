package de.codeinfection.quickwango.Anouncer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.Server;

/**
 *
 * @author CodeInfection
 */
public class AnouncerTask implements Runnable
{
    protected ArrayList<ArrayList<String>> messages;
    protected int current;
    protected Server server;

    public AnouncerTask(final Server server, final File dataFolder) throws IOException
    {
        final int maxLines = 20;
        this.current = 0;
        this.server = server;
        this.messages = new ArrayList<ArrayList<String>>();
        File messagesDir = new File(dataFolder, "scheduledMessages");
        messagesDir.mkdir();
        File[] messageFiles = messagesDir.listFiles(new TxtFilter());
        BufferedReader reader = null;
        String buffer = "";
        for (File messageFile : messageFiles)
        {
            try
            {
                ArrayList<String> lines = new ArrayList<String>();
                reader = new BufferedReader(new FileReader(messageFile));

                int counter = 0;
                while ((buffer = reader.readLine()) != null && counter < maxLines)
                {
                    lines.add(buffer.trim().replaceAll("&([a-f0-9])", "\u00A7$1"));
                    ++counter;
                }

                reader.close();
                this.messages.add(lines);
                Anouncer.debug("Loaded " + messageFile.getName() + "!");
            }
            catch (IOException e)
            {
                Anouncer.error("Failed to read " + messageFile.getName() + "!", e);
            }
        }
        if (this.messages.isEmpty())
        {
            throw new IOException("No message file found!");
        }
        Anouncer.log("Loaded " + this.messages.size() + " message files!");
    }

    protected ArrayList<String> nextMessage()
    {
        ArrayList<String> message = this.messages.get(current);
        ++this.current;
        if (current >= this.messages.size())
        {
            this.current = 0;
        }
        return message;
    }

    public void run()
    {
        for (String line : this.nextMessage())
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