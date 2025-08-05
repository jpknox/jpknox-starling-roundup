package jpknox.starling.roundup.dto.api.rest.transaction.feed;

import jpknox.starling.roundup.dto.api.rest.transaction.feed.item.FeedItem;

import java.util.List;

public record FeedItems(
        List<FeedItem> feedItems) {
}
