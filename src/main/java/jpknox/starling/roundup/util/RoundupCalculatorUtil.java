package jpknox.starling.roundup.util;

import jpknox.starling.roundup.dto.api.rest.transaction.feed.FeedItems;

import java.math.BigInteger;

public final class RoundupCalculatorUtil {

    private RoundupCalculatorUtil() {
    }

    /**
     * This assumes all the feed items will be in the same currency.
     * I used the example numbers in the spec and TDD to develop this method
     */
    public static BigInteger roundupAndSum(final FeedItems feedItems) {
        return feedItems
                .feedItems()
                .stream()
                .map(fi -> {
                    //Get two trailing digits and subtract from 100.
                    final BigInteger minorUnits = fi.amount().minorUnits().mod(BigInteger.valueOf(100));
                    if (minorUnits.equals(BigInteger.ZERO)) {
                        return BigInteger.ZERO;
                    }
                    return BigInteger.valueOf(100).subtract(minorUnits);
                })
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

}
