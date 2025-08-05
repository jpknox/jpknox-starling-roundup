package jpknox.starling.roundup.api.client;

import jpknox.starling.roundup.dto.api.rest.savings.goal.SavingsGoal;
import jpknox.starling.roundup.dto.api.rest.savings.goal.SavingsGoalList;
import jpknox.starling.roundup.dto.api.rest.savings.goal.request.SavingsGoalRequest;
import jpknox.starling.roundup.dto.api.rest.savings.goal.request.TopUpRequest;
import jpknox.starling.roundup.dto.api.rest.savings.goal.response.CreateOrUpdateSavingsGoalResponse;
import jpknox.starling.roundup.dto.api.rest.savings.goal.response.SavingsGoalTransferResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class SavingsGoalApiClient {

    private final RestClient restClient;

    @Autowired
    public SavingsGoalApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public SavingsGoalList getSavingsGoals(final UUID accountUid, final String token) {
        return restClient
                .get()
                .uri("/account/{accountUid}/savings-goals", accountUid)
                .header("Authorization", token)
                .retrieve()
                .body(SavingsGoalList.class);
    }

    public SavingsGoal getSavingsGoal(final UUID accountUid, final UUID savingsGoalUid, final String token) {
        return restClient
                .get()
                .uri("/account/{accountUid}/savings-goals/{savingsGoalUid}", accountUid, savingsGoalUid)
                .header("Authorization", token)
                .retrieve()
                .body(SavingsGoal.class);
    }

    public CreateOrUpdateSavingsGoalResponse createSavingsGoal(
            final UUID accountUid,
            final SavingsGoalRequest savingsGoalRequest,
            final String token) {
        return restClient
                .put()
                .uri("/account/{accountUid}/savings-goals", accountUid)
                .body(savingsGoalRequest)
                .header("Authorization", token)
                .retrieve()
                .body(CreateOrUpdateSavingsGoalResponse.class);
    }

    public SavingsGoalTransferResponse addMoneyIntoSavingsGoal(
            final UUID accountUid,
            final UUID savingsGoalUid,
            final UUID transferUid,
            final TopUpRequest topUpRequest,
            final String token) {
        return restClient
                .put()
                .uri("/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}",
                        accountUid, savingsGoalUid, transferUid)
                .body(topUpRequest)
                .header("Authorization", token)
                .retrieve()
                .body(SavingsGoalTransferResponse.class);
    }
}
