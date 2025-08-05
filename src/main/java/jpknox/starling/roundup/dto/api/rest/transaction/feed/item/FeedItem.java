package jpknox.starling.roundup.dto.api.rest.transaction.feed.item;

import jpknox.starling.roundup.dto.api.rest.transaction.feed.batch.BatchPaymentDetails;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

public record FeedItem(
        UUID feedItemUid,
        UUID categoryUid,
        Amount amount,
        Amount sourceAmount,
        Direction direction,
        LocalDateTime updatedAt,
        LocalDateTime transactionTime,
        LocalDateTime settlementTime,
        LocalDateTime retryAllocationUntilTime,
        Source source,
        SourceSubType sourceSubType,
        Status status,
        UUID transactingApplicationUserUid,
        CounterPartyType counterPartyType,
        UUID counterPartUid,
        String counterPartyName,
        UUID counterPartySubEntityUid,
        String counterPartySubEntityName,
        String counterPartySubEntityIdentifier,
        String counterPartySubEntitySubIdentifier,
        BigDecimal exchangeRate,
        BigInteger totalFees,
        Amount totalFeeAmount,
        String reference,
        Country country,
        SpendingCategory spendingCategory,
        String userNote,
        RoundUp roundUp,
        Boolean hasAttachment,
        Boolean hasReceipt,
        BatchPaymentDetails batchPaymentDetails) {
}
