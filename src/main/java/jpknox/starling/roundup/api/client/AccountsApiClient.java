package jpknox.starling.roundup.api.client;

import jpknox.starling.roundup.dto.api.rest.accounts.Accounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AccountsApiClient {

    private final RestClient restClient;

    @Autowired
    public AccountsApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public Accounts getAccounts(String token) {
        return restClient
                .get()
                .uri("/accounts")
                .header("Authorization", token)
                .retrieve()
                .body(Accounts.class);
    }

}
