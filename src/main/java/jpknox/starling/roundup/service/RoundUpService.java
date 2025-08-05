package jpknox.starling.roundup.service;

import jpknox.starling.roundup.api.client.AccountsApiClient;
import jpknox.starling.roundup.api.client.SavingsGoalApiClient;
import jpknox.starling.roundup.api.client.TransactionsApiClient;
import jpknox.starling.roundup.dto.api.rest.Currency;
import jpknox.starling.roundup.dto.api.rest.accounts.Accounts;
import jpknox.starling.roundup.dto.api.rest.roundup.request.RoundupRequest;
import jpknox.starling.roundup.dto.api.rest.roundup.response.RoundupResponse;
import jpknox.starling.roundup.dto.api.rest.savings.goal.CurrencyAndAmount;
import jpknox.starling.roundup.dto.api.rest.savings.goal.SavingsGoal;
import jpknox.starling.roundup.dto.api.rest.savings.goal.SavingsGoalList;
import jpknox.starling.roundup.dto.api.rest.savings.goal.request.SavingsGoalRequest;
import jpknox.starling.roundup.dto.api.rest.savings.goal.request.TopUpRequest;
import jpknox.starling.roundup.dto.api.rest.savings.goal.response.CreateOrUpdateSavingsGoalResponse;
import jpknox.starling.roundup.dto.api.rest.transaction.feed.FeedItems;
import jpknox.starling.roundup.util.ChronoUtil;
import jpknox.starling.roundup.util.RoundupCalculatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.UUID;

import static jpknox.starling.roundup.log.LogUtil.info;

@Service
public class RoundUpService {

    private final AccountsApiClient accountsApiClient;
    private final TransactionsApiClient transactionsApiClient;
    private final SavingsGoalApiClient savingsGoalAPIClient;

    @Autowired
    public RoundUpService(AccountsApiClient accountsApiClient,
                          TransactionsApiClient transactionsApiClient,
                          SavingsGoalApiClient savingsGoalAPIClient) {
        this.accountsApiClient = accountsApiClient;
        this.transactionsApiClient = transactionsApiClient;
        this.savingsGoalAPIClient = savingsGoalAPIClient;
    }

    public RoundupResponse roundUp(final RoundupRequest roundupRequest, final String token) {

        info("Processing round-up request for year:[%d], week:[%d]",
                roundupRequest.year(), roundupRequest.weekNumber());

        final ChronoUtil.WeekStartAndEnd weekStartAndEnd =
                ChronoUtil.getWeekStartAndEnd(roundupRequest.year(), roundupRequest.weekNumber());

        final UUID accountUid = getAccountUid(token);

        final FeedItems feedItems = getFeedItems(
                accountUid,
                weekStartAndEnd,
                token);

        final SavingsGoal savingsGoal = getSavingsGoal(accountUid, token);

        final BigInteger roundupResultMinorUnits = RoundupCalculatorUtil.roundupAndSum(feedItems);
        info("Round-up calculation is [%s]", roundupResultMinorUnits);

        addMoneyIntoSavingsGoal(accountUid, savingsGoal, roundupResultMinorUnits, token);

        info("Successfully completed processing for [%d] transactions", feedItems.feedItems().size());

        return new RoundupResponse(
                true,
                false,
                null,
                weekStartAndEnd.weekStart(),
                weekStartAndEnd.weekEnd(),
                roundupResultMinorUnits,
                BigInteger.valueOf(feedItems.feedItems().size()));
    }

    private UUID getAccountUid(final String token) {
        final Accounts accounts = accountsApiClient.getAccounts(token);
        return accounts.accounts().getFirst().accountUid();
    }

    private FeedItems getFeedItems(final UUID accountUid,
                                   final ChronoUtil.WeekStartAndEnd weekStartAndEnd,
                                   final String token) {
        final FeedItems feedItems = transactionsApiClient.getSettledTransactionsBetween(
                accountUid,
                weekStartAndEnd.weekStart(),
                weekStartAndEnd.weekEnd(),
                token);
        info("Found %d settled transactions within given timeframe", feedItems.feedItems().size());
        return feedItems;
    }

    private SavingsGoal getSavingsGoal(final UUID accountUid, final String token) {
        final SavingsGoalList savingsGoalList = savingsGoalAPIClient.getSavingsGoals(accountUid, token);
        final SavingsGoal savingsGoal;
        if (!savingsGoalList.savingsGoalList().isEmpty()) {
            savingsGoal = savingsGoalList.savingsGoalList().getFirst();
            info("Found a savings goal with ID:[%s]", savingsGoal.savingsGoalUid());
        } else {
            final CreateOrUpdateSavingsGoalResponse response = savingsGoalAPIClient.createSavingsGoal(accountUid,
                    new SavingsGoalRequest(
                            "My-Hello-World-Savings-Goal",
                            Currency.GBP,
                            new CurrencyAndAmount(Currency.GBP, BigInteger.valueOf(1000))),
                    token);
            savingsGoal = savingsGoalAPIClient.getSavingsGoal(accountUid, response.savingsGoalUid(), token);
            info("Could not find a savings goal, so created one with ID:[%s]", savingsGoal.savingsGoalUid());
        }
        return savingsGoal;
    }

    private void addMoneyIntoSavingsGoal(final UUID accountUid,
                                         final SavingsGoal savingsGoal,
                                         final BigInteger roundupResultMinorUnits, String token) {
        final UUID savingsGoalTransferAndReference = UUID.randomUUID();
        savingsGoalAPIClient.addMoneyIntoSavingsGoal(
                accountUid,
                savingsGoal.savingsGoalUid(),
                savingsGoalTransferAndReference,
                new TopUpRequest(
                        new CurrencyAndAmount(savingsGoal.target().currency(), roundupResultMinorUnits),
                        savingsGoalTransferAndReference.toString()),
                token);
    }

}
