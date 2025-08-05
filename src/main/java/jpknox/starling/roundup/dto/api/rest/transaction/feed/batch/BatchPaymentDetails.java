package jpknox.starling.roundup.dto.api.rest.transaction.feed.batch;

import java.util.UUID;

public record BatchPaymentDetails(
        UUID batchPaymentUid,
        BatchPaymentDetails batchPaymentDetails) {
}
