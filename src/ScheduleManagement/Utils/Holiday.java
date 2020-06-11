package ScheduleManagement.Utils;

import java.time.Month;

public class Holiday
{
    private final String name;
    private final int year;
    private final Month month;
    private final int day;

    public Holiday(String name, int year, Month month, int day)
    {
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getName()
    {
        return name;
    }

    public int getYear()
    {
        return year;
    }

    public Month getMonth()
    {
        return month;
    }

    public int getDay()
    {
        return day;
    }
}
