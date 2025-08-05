package jpknox.starling.roundup.dto.api.rest.savings.goal.response;

import java.util.UUID;

public record SavingsGoalTransferResponse(
        UUID transferUid,
        Boolean success
) {
}
