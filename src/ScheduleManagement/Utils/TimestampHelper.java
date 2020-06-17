package ScheduleManagement.Utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern(pattern).toFormatter();
        LocalDateTime ldt = formatter.parse(dateTime, LocalDateTime::from);
        return convertToUTC(ldt);
    }

    public static Timestamp convertToUTC(Timestamp timestamp)
    {
        return convertToUTC(timestamp.toLocalDateTime());
    }

    public static Timestamp convertToUTC(LocalDateTime ldt)
    {
        ZonedDateTime zdt = ldt.atZone(ZonedDateTime.now().getZone());
        return Timestamp.valueOf(zdt.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
    }

    public static Timestamp convertToLocal(String dateTime, String pattern)
    {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern(pattern).toFormatter();
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
        return Timestamp.valueOf(zdt.withZoneSameInstant(ZonedDateTime.now().getZone()).toLocalDateTime());
    }
}
