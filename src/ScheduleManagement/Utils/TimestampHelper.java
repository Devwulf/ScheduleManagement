package ScheduleManagement.Utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimestampHelper
{
    public static Timestamp now()
    {
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        return Timestamp.valueOf(ldt);
    }
}
