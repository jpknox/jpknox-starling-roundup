package jpknox.starling.roundup.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jpknox.starling.roundup.api.client.AccountsApiClient;
import jpknox.starling.roundup.api.client.SavingsGoalApiClient;
import jpknox.starling.roundup.api.client.TransactionsApiClient;
import jpknox.starling.roundup.dto.api.rest.Currency;
import jpknox.starling.roundup.dto.api.rest.accounts.Account;
import jpknox.starling.roundup.dto.api.rest.accounts.AccountType;
import jpknox.starling.roundup.dto.api.rest.accounts.Accounts;
import jpknox.starling.roundup.dto.api.rest.roundup.request.RoundupRequest;
import jpknox.starling.roundup.dto.api.rest.roundup.response.RoundupResponse;
import jpknox.starling.roundup.dto.api.rest.savings.goal.SavingsGoal;
import jpknox.starling.roundup.dto.api.rest.savings.goal.SavingsGoalList;
import jpknox.starling.roundup.dto.api.rest.savings.goal.SavingsGoalState;
import jpknox.starling.roundup.dto.api.rest.savings.goal.response.SavingsGoalTransferResponse;
import jpknox.starling.roundup.dto.api.rest.transaction.feed.FeedItems;
import jpknox.starling.roundup.dto.api.rest.transaction.feed.item.Amount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static jpknox.starling.roundup.util.RoundupCalculatorUtilTest.feedItemWithAmount;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoundupControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoSpyBean
    private AccountsApiClient accountsApiClient;

    @MockitoSpyBean
    private TransactionsApiClient transactionsApiClient;

    @MockitoSpyBean
    private SavingsGoalApiClient savingsGoalAPiClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void activateRoundup() throws JsonProcessingException {

        final Account mockEmployee = createAccount("1");
        doReturn(new Accounts(List.of(mockEmployee))).when(accountsApiClient).getAccounts(any());

        final FeedItems feedItems = new FeedItems(List.of(
                feedItemWithAmount(BigInteger.valueOf(435)),
                feedItemWithAmount(BigInteger.valueOf(520)),
                feedItemWithAmount(BigInteger.valueOf(87))));
        doReturn(feedItems).when(transactionsApiClient).getSettledTransactionsBetween(any(), any(), any(), any());

        final SavingsGoal savingsGoal = new SavingsGoal(
                UUID.randomUUID(),
                "Savings-Goal",
                new Amount(Currency.GBP, BigInteger.valueOf(100)),
                new Amount(Currency.GBP, BigInteger.ZERO),
                0,
                SavingsGoalState.ACTIVE);
        doReturn(new SavingsGoalList(List.of(savingsGoal))).when(savingsGoalAPiClient).getSavingsGoals(any(), any());

        final SavingsGoalTransferResponse savingsGoalTransferResponse = new SavingsGoalTransferResponse(
                UUID.randomUUID(), Boolean.TRUE);
        doReturn(savingsGoalTransferResponse).when(savingsGoalAPiClient).addMoneyIntoSavingsGoal(
                any(), any(), any(), any(), any());

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer abc.123.xyz");
        final ResponseEntity<RoundupResponse> responseEntity = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/round-up/trigger",
                new HttpEntity<>(new RoundupRequest(2025, 31), headers),
                RoundupResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        final RoundupResponse roundupResponse = responseEntity.getBody();
        Assertions.assertNotNull(roundupResponse);
        assertTrue(roundupResponse.success());
        assertFalse(roundupResponse.error());
        assertEquals(BigInteger.valueOf(158), roundupResponse.roundupSum());
        assertEquals(BigInteger.valueOf(3), roundupResponse.settledTransactionsProcessed());
    }

    @Test
    void handleException() {

        doThrow(RuntimeException.class).when(accountsApiClient).getAccounts(any());

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer abc.123.xyz");
        final ResponseEntity<RoundupResponse> responseEntity = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/round-up/trigger",
                new HttpEntity<>(new RoundupRequest(2025, 31), headers),
                RoundupResponse.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        final RoundupResponse roundupResponse = responseEntity.getBody();
        Assertions.assertNotNull(roundupResponse);
        assertFalse(roundupResponse.success());
        assertTrue(roundupResponse.error());
        assertNotNull(roundupResponse.errorMessage());
    }

    private Account createAccount(final String suffix) {
        final UUID accountId = UUID.randomUUID();
        final UUID defaultCategoryId = UUID.randomUUID();
        final LocalDateTime now = LocalDateTime.now();
        return new Account(
                accountId,
                AccountType.PRIMARY,
                defaultCategoryId,
                Currency.GBP,
                now,
                "name-" + suffix);
    }
}