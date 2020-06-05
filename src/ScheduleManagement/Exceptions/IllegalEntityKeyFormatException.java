package ScheduleManagement.Exceptions;

public class IllegalEntityKeyFormatException extends Exception
{
    public IllegalEntityKeyFormatException(String errorMessage)
    {
        super(errorMessage);
    }
}
