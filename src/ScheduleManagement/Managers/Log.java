package ScheduleManagement.Managers;

import ScheduleManagement.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class Log
{
    // Message should not have \n at the end
    public static void logToFile(String message)
    {
        // Logs to file "log.txt"
        logToFile(message, "log");
    }

    // Filename should not have the file extension
    public static void logToFile(String message, String fileName)
    {
        try
        {
            String msg = message + "\n";
            File file = new File(getWorkingDirectory() + "\\" + fileName + ".txt");
            if (!file.exists())
                file.createNewFile();

            FileOutputStream stream = new FileOutputStream(file.getPath(), true);
            stream.write(msg.getBytes());
            stream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String getWorkingDirectory()
    {
        return System.getProperty("user.dir");
    }
}
