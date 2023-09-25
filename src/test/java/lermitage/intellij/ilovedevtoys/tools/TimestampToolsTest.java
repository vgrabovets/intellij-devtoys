package lermitage.intellij.ilovedevtoys.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimestampToolsTest {

    @Test
    void should_return_now_as_timestamp() {
        Assertions.assertTrue(TimestampTools.getNowAsTimestampSec() > 0);
    }

    @Test
    void should_return_timestamp_as_human_date() {
        Assertions.assertFalse(TimestampTools.getTimeStampAsHumanDatetime(1667016916L, "America/Montreal", true).contains("1970"));
    }

    @Test
    void should_explode_timestamp_sec() {
        Assertions.assertNotEquals(1970, TimestampTools.toTimestampFields(1667016916L, true).year());
    }

    @Test
    void should_explode_timestamp_ms() {
        Assertions.assertNotEquals(1970, TimestampTools.toTimestampFields(1667016916123L, true).year());
    }

    @Test
    void test_relative_time() {
        LocalDateTime fixedNow = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        MockedStatic<LocalDateTime> mock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
        Mockito.when(LocalDateTime.now(ZoneId.systemDefault())).thenReturn(fixedNow);

        Assertions.assertEquals("Right now", TimestampTools.getRelativeTime(fixedNow));
        Assertions.assertEquals("1 hour in the future", TimestampTools.getRelativeTime(fixedNow.plusHours(1)));
        Assertions.assertEquals("59 seconds in the future", TimestampTools.getRelativeTime(fixedNow.plusSeconds(59)));
        Assertions.assertEquals("1 minute in the future", TimestampTools.getRelativeTime(fixedNow.plusSeconds(60)));
        Assertions.assertEquals("1 minute in the future", TimestampTools.getRelativeTime(fixedNow.plusSeconds(61)));
        Assertions.assertEquals("1 minute in the future", TimestampTools.getRelativeTime(fixedNow.plusSeconds(119)));
        Assertions.assertEquals("2 minutes in the future", TimestampTools.getRelativeTime(fixedNow.plusSeconds(120)));

        Assertions.assertEquals("59 minutes ago", TimestampTools.getRelativeTime(fixedNow.minusMinutes(59)));
        Assertions.assertEquals("1 hour ago", TimestampTools.getRelativeTime(fixedNow.minusMinutes(60)));
        Assertions.assertEquals("111 years ago", TimestampTools.getRelativeTime(fixedNow.minusYears(111)));
        Assertions.assertEquals("111 years ago", TimestampTools.getRelativeTime(fixedNow.minusYears(111).minusDays(50)));

        mock.close();
    }
}
