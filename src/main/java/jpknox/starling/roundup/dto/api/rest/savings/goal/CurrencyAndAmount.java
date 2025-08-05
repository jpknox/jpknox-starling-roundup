package jpknox.starling.roundup.dto.api.rest.savings.goal;

import jpknox.starling.roundup.dto.api.rest.Currency;

import java.math.BigInteger;

public record CurrencyAndAmount(
        Currency currency,
        BigInteger minorUnits
) {
}
