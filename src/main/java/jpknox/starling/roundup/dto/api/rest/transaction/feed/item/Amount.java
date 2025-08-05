package jpknox.starling.roundup.dto.api.rest.transaction.feed.item;

import jpknox.starling.roundup.dto.api.rest.Currency;

import java.math.BigInteger;

public record Amount(
        Currency currency,
        BigInteger minorUnits
) {
}
