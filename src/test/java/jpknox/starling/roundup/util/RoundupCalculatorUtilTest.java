package jpknox.starling.roundup.util;

import jpknox.starling.roundup.dto.api.rest.Currency;
import jpknox.starling.roundup.dto.api.rest.transaction.feed.FeedItems;
import jpknox.starling.roundup.dto.api.rest.transaction.feed.item.Amount;
import jpknox.starling.roundup.dto.api.rest.transaction.feed.item.FeedItem;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoundupCalculatorUtilTest {

    //I used the example numbers in the spec and TDD to develop this method
    @Test
    void roundupAndSum_exampleInChallengeSpec() {
        final FeedItems feedItems = new FeedItems(List.of(
                feedItemWithAmount(BigInteger.valueOf(435)),
                feedItemWithAmount(BigInteger.valueOf(520)),
                feedItemWithAmount(BigInteger.valueOf(87))));
        assertEquals(BigInteger.valueOf(158), RoundupCalculatorUtil.roundupAndSum(feedItems));
    }

    @Test
    void roundupAndSum_pennies() {
        final FeedItems feedItems = new FeedItems(List.of(
                feedItemWithAmount(BigInteger.valueOf(5)),
                feedItemWithAmount(BigInteger.valueOf(4)),
                feedItemWithAmount(BigInteger.valueOf(3))));
        assertEquals(BigInteger.valueOf(288), RoundupCalculatorUtil.roundupAndSum(feedItems));
    }

    @Test
    void roundupAndSum_nothingToRoundUp() {
        final FeedItems feedItems = new FeedItems(List.of(
                feedItemWithAmount(BigInteger.valueOf(100)),
                feedItemWithAmount(BigInteger.valueOf(100)),
                feedItemWithAmount(BigInteger.valueOf(100))));
        assertEquals(BigInteger.valueOf(0), RoundupCalculatorUtil.roundupAndSum(feedItems));
    }

    @Test
    void roundupAndSum_zeros() {
        final FeedItems feedItems = new FeedItems(List.of(
                feedItemWithAmount(BigInteger.valueOf(0)),
                feedItemWithAmount(BigInteger.valueOf(0)),
                feedItemWithAmount(BigInteger.valueOf(0))));
        assertEquals(BigInteger.valueOf(0), RoundupCalculatorUtil.roundupAndSum(feedItems));
    }

    @Test
    void roundupAndSum_billion() {
        final FeedItems feedItems = new FeedItems(List.of(
                feedItemWithAmount(BigInteger.valueOf(100000000050L)),
                feedItemWithAmount(BigInteger.valueOf(100000000070L)),
                feedItemWithAmount(BigInteger.valueOf(100000000080L))));
        assertEquals(BigInteger.valueOf(100), RoundupCalculatorUtil.roundupAndSum(feedItems));
    }

    public static FeedItem feedItemWithAmount(final BigInteger amount) {
        return new FeedItem(null,
                null,
                new Amount(Currency.GBP, amount),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}