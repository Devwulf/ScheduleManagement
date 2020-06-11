package ScheduleManagement.Utils;

import java.time.Month;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class HolidayRule
{
    private final String holidayName;
    private final Month month;
    private final UnaryOperator<Integer> dayRule;

    public HolidayRule(String holidayName, Month month, UnaryOperator<Integer> dayRule)
    {
        this.holidayName = holidayName;
        this.month = month;
        this.dayRule = dayRule;
    }

    public String getHolidayName()
    {
        return holidayName;
    }

    public Month getMonth()
    {
        return month;
    }

    public int getDay(int year)
    {
        return dayRule.apply(year);
    }
}
