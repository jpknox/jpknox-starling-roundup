package jpknox.starling.roundup.dto.api.rest.savings.goal;

import jpknox.starling.roundup.dto.api.rest.transaction.feed.item.Amount;

import java.util.UUID;

public record SavingsGoal(
        UUID savingsGoalUid,
        String name,
        Amount target,
        Amount totalSaved,
        Integer savedPercentage,
        SavingsGoalState state
) {
}
