package ScheduleManagement.Utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Holidays
{
    private static final List<HolidayRule> holidayRules;

    static
    {
        holidayRules = new ArrayList<>();
        holidayRules.add(new HolidayRule("New Year's Day", Month.JANUARY, (year) ->
        {
            return 1;
        }));

        holidayRules.add(new HolidayRule("Martin Luther King Jr. Day", Month.JANUARY, (year) ->
        {
            return getDayOfNthWeek(year, Month.JANUARY, 3, DayOfWeek.MONDAY);
        }));

        holidayRules.add(new HolidayRule("Valentine's Day", Month.FEBRUARY, (year) ->
        {
            return 14;
        }));

        holidayRules.add(new HolidayRule("Presidents' Day", Month.FEBRUARY, (year) ->
        {
            return getDayOfNthWeek(year, Month.FEBRUARY, 3, DayOfWeek.MONDAY);
        }));

        holidayRules.add(new HolidayRule("St. Patrick's Day", Month.MARCH, (year) ->
        {
            return 17;
        }));

        holidayRules.add(new HolidayRule("April Fools' Day", Month.APRIL, (year) ->
        {
            return 1;
        }));

        holidayRules.add(new HolidayRule("Cinco de Mayo", Month.MAY, (year) ->
        {
            return 5;
        }));

        holidayRules.add(new HolidayRule("Mother's Day", Month.MAY, (year) ->
        {
            return getDayOfNthWeek(year, Month.MAY, 2, DayOfWeek.SUNDAY);
        }));

        holidayRules.add(new HolidayRule("Memorial Day", Month.MAY, (year) ->
        {
            return getDayOfNthWeek(year, Month.MAY, -1, DayOfWeek.MONDAY);
        }));

        holidayRules.add(new HolidayRule("Father's Day", Month.JUNE, (year) ->
        {
            return getDayOfNthWeek(year, Month.JUNE, 3, DayOfWeek.SUNDAY);
        }));

        holidayRules.add(new HolidayRule("Independence Day", Month.JULY, (year) ->
        {
            return 4;
        }));

        holidayRules.add(new HolidayRule("Labor Day", Month.SEPTEMBER, (year) ->
        {
            return getDayOfNthWeek(year, Month.SEPTEMBER, 1, DayOfWeek.MONDAY);
        }));

        holidayRules.add(new HolidayRule("Columbus Day", Month.OCTOBER, (year) ->
        {
            return getDayOfNthWeek(year, Month.OCTOBER, 2, DayOfWeek.MONDAY);
        }));

        holidayRules.add(new HolidayRule("Halloween", Month.OCTOBER, (year) ->
        {
            LocalDate date = LocalDate.of(year, Month.OCTOBER, 1);
            date = date.with(TemporalAdjusters.lastDayOfMonth());
            return date.getDayOfMonth();
        }));

        holidayRules.add(new HolidayRule("Election Day", Month.NOVEMBER, (year) ->
        {
            LocalDate date = LocalDate.of(year, Month.NOVEMBER, 1);
            date = date.with(TemporalAdjusters.dayOfWeekInMonth(date.getDayOfWeek() == DayOfWeek.TUESDAY ? 2 : 1, DayOfWeek.TUESDAY));
            return date.getDayOfMonth();
        }));

        holidayRules.add(new HolidayRule("Veterans Day", Month.NOVEMBER, (year) ->
        {
            return 11;
        }));

        holidayRules.add(new HolidayRule("Thanksgiving Day", Month.NOVEMBER, (year) ->
        {
            return getDayOfNthWeek(year, Month.NOVEMBER, 4, DayOfWeek.THURSDAY);
        }));

        holidayRules.add(new HolidayRule("Black Friday", Month.NOVEMBER, (year) ->
        {
            return getDayOfNthWeek(year, Month.NOVEMBER, 4, DayOfWeek.FRIDAY);
        }));

        holidayRules.add(new HolidayRule("Christmas Eve", Month.DECEMBER, (year) ->
        {
            return 24;
        }));

        holidayRules.add(new HolidayRule("Christmas Day", Month.DECEMBER, (year) ->
        {
            return 25;
        }));

        holidayRules.add(new HolidayRule("New Year's Eve", Month.DECEMBER, (year) ->
        {
            LocalDate date = LocalDate.of(year, Month.DECEMBER, 1);
            date = date.with(TemporalAdjusters.lastDayOfMonth());
            return date.getDayOfMonth();
        }));
    }

    public static List<Holiday> getHolidays(int year)
    {
        return holidayRules.stream()
                           .map(holidayRule -> new Holiday(holidayRule.getHolidayName(), LocalDate.of(year, holidayRule.getMonth(), holidayRule.getDay(year))))
                           .collect(Collectors.toList());
    }

    public static List<Holiday> getHolidays(int year, Month month)
    {
        return holidayRules.stream()
                           .filter(holidayRule -> holidayRule.getMonth()
                                                             .equals(month))
                           .map(holidayRule -> new Holiday(holidayRule.getHolidayName(), LocalDate.of(year, holidayRule.getMonth(), holidayRule.getDay(year))))
                           .collect(Collectors.toList());
    }

    // Gets all holidays in a specific range of days, at most 6 days difference
    public static List<Holiday> getHolidaysInWeek(LocalDate startWeek, LocalDate endWeek)
    {
        List<Holiday> holidays = new ArrayList<>();
        if (startWeek.compareTo(endWeek) > 0)
            return holidays;
        if (ChronoUnit.DAYS.between(startWeek, endWeek) > 6)
            return holidays;

        if (!startWeek.getMonth()
                       .equals(endWeek.getMonth()))
        {
            List<HolidayRule> startRules = holidayRules.stream()
                                                       .filter(holidayRule -> holidayRule.getMonth()
                                                                                         .equals(startWeek.getMonth()))
                                                       .collect(Collectors.toList());
            List<HolidayRule> endRules = holidayRules.stream()
                                                     .filter(holidayRule -> holidayRule.getMonth()
                                                                                       .equals(endWeek.getMonth()))
                                                     .collect(Collectors.toList());
            for (HolidayRule rule : startRules)
            {
                LocalDate holiday = LocalDate.of(startWeek.getYear(), startWeek.getMonth(), rule.getDay(startWeek.getYear()));
                if (holiday.isEqual(startWeek) || holiday.isAfter(startWeek))
                    holidays.add(new Holiday(rule.getHolidayName(), holiday));
            }
            for (HolidayRule rule : endRules)
            {
                LocalDate holiday = LocalDate.of(endWeek.getYear(), endWeek.getMonth(), rule.getDay(endWeek.getYear()));
                if (holiday.isEqual(endWeek) || holiday.isBefore(endWeek))
                    holidays.add(new Holiday(rule.getHolidayName(), holiday));
            }
        }
        else
        {
            List<HolidayRule> monthRules = holidayRules.stream()
                                                       .filter(holidayRule -> holidayRule.getMonth()
                                                                                         .equals(startWeek.getMonth()))
                                                       .collect(Collectors.toList());
            for (HolidayRule rule : monthRules)
            {
                LocalDate holiday = LocalDate.of(startWeek.getYear(), startWeek.getMonth(), rule.getDay(startWeek.getYear()));
                if (TimestampHelper.isDateInBetween(holiday, startWeek, endWeek))
                    holidays.add(new Holiday(rule.getHolidayName(), holiday));
            }
        }

        return holidays;
    }

    private static int getDayOfNthWeek(int year, Month month, int week, DayOfWeek day)
    {
        LocalDate date = LocalDate.of(year, month, 1);
        date = date.with(TemporalAdjusters.dayOfWeekInMonth(week, day));
        return date.getDayOfMonth();
    }
}
