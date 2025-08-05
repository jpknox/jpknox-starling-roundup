package jpknox.starling.roundup.dto.api.rest.savings.goal.request;

import jpknox.starling.roundup.dto.api.rest.savings.goal.CurrencyAndAmount;

public record TopUpRequest(
        CurrencyAndAmount amount,
        String reference) {
}
