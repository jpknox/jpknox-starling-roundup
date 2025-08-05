package jpknox.starling.roundup.util;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class ChronoUtilTest {

    @Test
    void getWeekStartAndEnd_2025_31() {
        final ChronoUtil.WeekStartAndEnd result = ChronoUtil.getWeekStartAndEnd(2025, 31);
        assertEquals(OffsetDateTime.parse("2025-07-28T00:00Z"), result.weekStart());
        assertEquals(OffsetDateTime.parse("2025-08-03T23:59:59.999999999Z"), result.weekEnd());
    }

    @Test
    void getWeekStartAndEnd_2025_32() {
        final ChronoUtil.WeekStartAndEnd result = ChronoUtil.getWeekStartAndEnd(2025, 32);
        assertEquals(OffsetDateTime.parse("2025-08-04T00:00Z"), result.weekStart());
        assertEquals(OffsetDateTime.parse("2025-08-10T23:59:59.999999999Z"), result.weekEnd());
    }
}