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
        // Returns 1 for a given year
        holidayRules.add(new HolidayRule("New Year's Day", Month.JANUARY, (year) ->
        {
            return 1;
        }));

        // Returns the day of the 3rd Monday of January for a given year
        holidayRules.add(new HolidayRule("Martin Luther King Jr. Day", Month.JANUARY, (year) ->
        {
            return getDayOfNthWeek(year, Month.JANUARY, 3, DayOfWeek.MONDAY);
        }));

        // Returns 14 for a given year
        holidayRules.add(new HolidayRule("Valentine's Day", Month.FEBRUARY, (year) ->
        {
            return 14;
        }));

        // Returns the day of the 3rd Monday of February for a given year
        holidayRules.add(new HolidayRule("Presidents' Day", Month.FEBRUARY, (year) ->
        {
            return getDayOfNthWeek(year, Month.FEBRUARY, 3, DayOfWeek.MONDAY);
        }));

        // Returns 17 for a given year
        holidayRules.add(new HolidayRule("St. Patrick's Day", Month.MARCH, (year) ->
        {
            return 17;
        }));

        // Returns 1 for a given year
        holidayRules.add(new HolidayRule("April Fools' Day", Month.APRIL, (year) ->
        {
            return 1;
        }));

        // Returns 5 for a given year
        holidayRules.add(new HolidayRule("Cinco de Mayo", Month.MAY, (year) ->
        {
            return 5;
        }));

        // Returns the day of the 2nd Sunday of May for a given year
        holidayRules.add(new HolidayRule("Mother's Day", Month.MAY, (year) ->
        {
            return getDayOfNthWeek(year, Month.MAY, 2, DayOfWeek.SUNDAY);
        }));

        // Returns the day of the last Monday of May for a given year
        holidayRules.add(new HolidayRule("Memorial Day", Month.MAY, (year) ->
        {
            return getDayOfNthWeek(year, Month.MAY, -1, DayOfWeek.MONDAY);
        }));

        // Returns the day of the 3rd Sunday of June for a given year
        holidayRules.add(new HolidayRule("Father's Day", Month.JUNE, (year) ->
        {
            return getDayOfNthWeek(year, Month.JUNE, 3, DayOfWeek.SUNDAY);
        }));

        // Returns 4 for a given year
        holidayRules.add(new HolidayRule("Independence Day", Month.JULY, (year) ->
        {
            return 4;
        }));

        // Returns the day of the 1st Monday of September for a given year
        holidayRules.add(new HolidayRule("Labor Day", Month.SEPTEMBER, (year) ->
        {
            return getDayOfNthWeek(year, Month.SEPTEMBER, 1, DayOfWeek.MONDAY);
        }));

        // Returns the day of the 2nd Monday of October for a given year
        holidayRules.add(new HolidayRule("Columbus Day", Month.OCTOBER, (year) ->
        {
            return getDayOfNthWeek(year, Month.OCTOBER, 2, DayOfWeek.MONDAY);
        }));

        // Returns the last day of October for a given year
        holidayRules.add(new HolidayRule("Halloween", Month.OCTOBER, (year) ->
        {
            LocalDate date = LocalDate.of(year, Month.OCTOBER, 1);
            date = date.with(TemporalAdjusters.lastDayOfMonth());
            return date.getDayOfMonth();
        }));

        // Returns the first Tuesday of November that's not November 1 for a given year
        holidayRules.add(new HolidayRule("Election Day", Month.NOVEMBER, (year) ->
        {
            LocalDate date = LocalDate.of(year, Month.NOVEMBER, 1);
            date = date.with(TemporalAdjusters.dayOfWeekInMonth(date.getDayOfWeek() == DayOfWeek.TUESDAY ? 2 : 1, DayOfWeek.TUESDAY));
            return date.getDayOfMonth();
        }));

        // Returns 11 for a given year
        holidayRules.add(new HolidayRule("Veterans Day", Month.NOVEMBER, (year) ->
        {
            return 11;
        }));

        // Returns the day of the 4th Thursday of November for a given year
        holidayRules.add(new HolidayRule("Thanksgiving Day", Month.NOVEMBER, (year) ->
        {
            return getDayOfNthWeek(year, Month.NOVEMBER, 4, DayOfWeek.THURSDAY);
        }));

        // Returns the day of the 4th Friday of November for a given year
        holidayRules.add(new HolidayRule("Black Friday", Month.NOVEMBER, (year) ->
        {
            return getDayOfNthWeek(year, Month.NOVEMBER, 4, DayOfWeek.FRIDAY);
        }));

        // Returns 24 for a given year
        holidayRules.add(new HolidayRule("Christmas Eve", Month.DECEMBER, (year) ->
        {
            return 24;
        }));

        // Returns 25 for a given year
        holidayRules.add(new HolidayRule("Christmas Day", Month.DECEMBER, (year) ->
        {
            return 25;
        }));

        // Returns the last day of December for a given year
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
                           // Converts all the holiday rules to actual holidays for the given year
                           .map(holidayRule -> new Holiday(holidayRule.getHolidayName(), LocalDate.of(year, holidayRule.getMonth(), holidayRule.getDay(year))))
                           .collect(Collectors.toList());
    }

    public static List<Holiday> getHolidays(int year, Month month)
    {
        return holidayRules.stream()
                           // Keeps all the holiday rules for the given month
                           .filter(holidayRule -> holidayRule.getMonth()
                                                             .equals(month))
                           // Converts all the holiday rules to actual holidays for the given year and month
                           .map(holidayRule -> new Holiday(holidayRule.getHolidayName(), LocalDate.of(year, holidayRule.getMonth(), holidayRule.getDay(year))))
                           .collect(Collectors.toList());
    }

    // Gets all holidays in a specific range of days, at most 6 days difference
    public static List<Holiday> getHolidaysInWeek(LocalDate startDate, LocalDate endDate)
    {
        List<Holiday> holidays = new ArrayList<>();
        if (startDate.compareTo(endDate) > 0)
            return holidays;
        if (ChronoUnit.DAYS.between(startDate, endDate) > 6)
            return holidays;

        if (!startDate.getMonth()
                      .equals(endDate.getMonth()))
        {
            List<HolidayRule> startRules = holidayRules.stream()
                                                       // Keeps all the holiday rules with the same month as the given
                                                       // start date of the week
                                                       .filter(holidayRule -> holidayRule.getMonth()
                                                                                         .equals(startDate.getMonth()))
                                                       .collect(Collectors.toList());
            List<HolidayRule> endRules = holidayRules.stream()
                                                     // Keeps all the holiday rules with the same month as the given
                                                     // end date of the week (for weeks crossing over between months)
                                                     .filter(holidayRule -> holidayRule.getMonth()
                                                                                       .equals(endDate.getMonth()))
                                                     .collect(Collectors.toList());
            for (HolidayRule rule : startRules)
            {
                LocalDate holiday = LocalDate.of(startDate.getYear(), startDate.getMonth(), rule.getDay(startDate.getYear()));
                if (holiday.isEqual(startDate) || holiday.isAfter(startDate))
                    holidays.add(new Holiday(rule.getHolidayName(), holiday));
            }
            for (HolidayRule rule : endRules)
            {
                LocalDate holiday = LocalDate.of(endDate.getYear(), endDate.getMonth(), rule.getDay(endDate.getYear()));
                if (holiday.isEqual(endDate) || holiday.isBefore(endDate))
                    holidays.add(new Holiday(rule.getHolidayName(), holiday));
            }
        }
        else
        {
            List<HolidayRule> monthRules = holidayRules.stream()
                                                       // Keeps all the holiday rules with the same month as the given
                                                       // start date of the week
                                                       .filter(holidayRule -> holidayRule.getMonth()
                                                                                         .equals(startDate.getMonth()))
                                                       .collect(Collectors.toList());
            for (HolidayRule rule : monthRules)
            {
                LocalDate holiday = LocalDate.of(startDate.getYear(), startDate.getMonth(), rule.getDay(startDate.getYear()));
                if (TimestampHelper.isDateInBetween(holiday, startDate, endDate))
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
