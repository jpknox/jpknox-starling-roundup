package jpknox.starling.roundup.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.WeekFields;

import static jpknox.starling.roundup.log.LogUtil.info;

public final class ChronoUtil {

    private ChronoUtil() {
    }

    public static WeekStartAndEnd getWeekStartAndEnd(int year, int weekNumber) {
        final WeekFields weekFields = WeekFields.ISO;
        final LocalDate startOfWeek = LocalDate
                .now()
                .withYear(year)
                .with(weekFields.weekOfYear(), weekNumber)
                .with(weekFields.dayOfWeek(), 1);
        final LocalDate endOfWeek = startOfWeek.plusDays(6);
        final OffsetDateTime startDateTime = startOfWeek.atStartOfDay().atOffset(ZoneOffset.UTC);
        final OffsetDateTime endDateTime = endOfWeek.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);

        info("Converted year:[%d] and week:[%d] to minTransactionTimestamp:[%s], maxTransactionTimestamp:[%s]",
                year, weekNumber, startDateTime, endDateTime);

        return new WeekStartAndEnd(startDateTime, endDateTime);

    }

    public record WeekStartAndEnd(
            OffsetDateTime weekStart,
            OffsetDateTime weekEnd) {
    }

}
