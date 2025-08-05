package jpknox.starling.roundup.dto.api.rest.roundup.response;

import java.math.BigInteger;
import java.time.OffsetDateTime;

public record RoundupResponse(
        Boolean success,
        Boolean error,
        String errorMessage,
        OffsetDateTime minTransactionTimestamp,
        OffsetDateTime maxTransactionTimestamp,
        BigInteger roundupSum,
        BigInteger settledTransactionsProcessed) {
}
