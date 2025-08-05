package jpknox.starling.roundup.dto.api.rest.transaction.feed.item;

import java.util.UUID;

public record RoundUp(
        UUID goalCategoryUid,
        Amount amount
) {
}
