package ScheduleManagement.Utils;

import java.time.LocalDate;
import java.time.Month;

public class Holiday
{
    private final String name;
    private final LocalDate date;

    public Holiday(String name, LocalDate date)
    {
        this.name = name;
        this.date = date;
    }

    public String getName()
    {
        return name;
    }

    public LocalDate getDate()
    {
        return date;
    }
}
