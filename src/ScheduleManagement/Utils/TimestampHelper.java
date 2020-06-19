package ScheduleManagement.Utils;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class TimestampHelper
{
    public static Timestamp now()
    {
        LocalDateTime ldt = ZonedDateTime.now()
                                         .toLocalDateTime();
        return Timestamp.valueOf(ldt);
    }

    public static Timestamp nowUTC()
    {
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        return Timestamp.valueOf(ldt);
    }

    public static Timestamp convertToUTC(String dateTime, String pattern)
    {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern(pattern)
                                                                    .toFormatter();
        LocalDateTime ldt = formatter.parse(dateTime, LocalDateTime::from);
        return convertToUTC(ldt);
    }

    public static Timestamp convertToUTC(Timestamp timestamp)
    {
        return convertToUTC(timestamp.toLocalDateTime());
    }

    public static Timestamp convertToUTC(LocalDateTime ldt)
    {
        ZonedDateTime zdt = ldt.atZone(ZonedDateTime.now()
                                                    .getZone());
        return Timestamp.valueOf(zdt.withZoneSameInstant(ZoneOffset.UTC)
                                    .toLocalDateTime());
    }

    public static Timestamp convertToLocal(String dateTime, String pattern)
    {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern(pattern)
                                                                    .toFormatter();
        LocalDateTime ldt = formatter.parse(dateTime, LocalDateTime::from);
        return convertToLocal(ldt);
    }

    public static Timestamp convertToLocal(Timestamp timestamp)
    {
        return convertToLocal(timestamp.toLocalDateTime());
    }

    public static Timestamp convertToLocal(LocalDateTime ldt)
    {
        ZonedDateTime zdt = ldt.atZone(ZoneOffset.UTC);
        return Timestamp.valueOf(zdt.withZoneSameInstant(ZonedDateTime.now()
                                                                      .getZone())
                                    .toLocalDateTime());
    }

    public static boolean isTimeOverlapping(Timestamp start1, Timestamp end1, Timestamp start2, Timestamp end2)
    {
        return start1.before(end2) && start2.before(end1);
    }

    public static boolean isDateInBetween(LocalDate date, LocalDate startRange, LocalDate endRange)
    {
        return (startRange.isEqual(date) || startRange.isBefore(date)) && (endRange.isEqual(date) || endRange.isAfter(date));
    }
}
