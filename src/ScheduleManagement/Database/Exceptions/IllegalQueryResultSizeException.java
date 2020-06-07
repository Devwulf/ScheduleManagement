package ScheduleManagement.Database.Exceptions;

public class IllegalQueryResultSizeException extends Exception
{
    public IllegalQueryResultSizeException(String errorMessage)
    {
        super(errorMessage);
    }
}
