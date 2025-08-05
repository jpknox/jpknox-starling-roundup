package jpknox.starling.roundup.dto.api.rest.savings.goal.request;

import jpknox.starling.roundup.dto.api.rest.Currency;
import jpknox.starling.roundup.dto.api.rest.savings.goal.CurrencyAndAmount;

public record SavingsGoalRequest(
        String name,
        Currency currency,
        CurrencyAndAmount target
) {
}
