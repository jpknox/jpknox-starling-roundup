package jpknox.starling.roundup.api.client;

import jpknox.starling.roundup.dto.api.rest.transaction.feed.FeedItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class TransactionsApiClient {

    private final RestClient restClient;

    @Autowired
    public TransactionsApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public FeedItems getSettledTransactionsBetween(
            final UUID accountUid,
            final OffsetDateTime minTransactionTimestamp,
            final OffsetDateTime maxTransactionTimestamp,
            final String token) {
        return restClient
                .get()
                .uri("/feed/account/{accountUid}/settled-transactions-between" +
                                "?minTransactionTimestamp={minTransactionTimestamp}" +
                                "&maxTransactionTimestamp={maxTransactionTimestamp}",
                        accountUid, minTransactionTimestamp, maxTransactionTimestamp)
                .header("Authorization", token)
                .retrieve()
                .body(FeedItems.class);
    }

}
