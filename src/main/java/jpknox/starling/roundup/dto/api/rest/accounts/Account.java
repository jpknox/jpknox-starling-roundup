package jpknox.starling.roundup.dto.api.rest.accounts;

import jpknox.starling.roundup.dto.api.rest.Currency;

import java.time.LocalDateTime;
import java.util.UUID;

public record Account(
        UUID accountUid,
        AccountType accountType,
        UUID defaultCategory,
        Currency currency,
        LocalDateTime createdAt,
        String name) {
}
