package jpknox.starling.roundup.dto.api.rest.accounts;

import jpknox.starling.roundup.dto.api.rest.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

class AccountsTest {

    @Test
    void testConstructor() {
        final UUID accountId1 = UUID.randomUUID();
        final UUID defaultCategoryId1 = UUID.randomUUID();
        final LocalDateTime now1 = LocalDateTime.now();
        final Account account1 = new Account(
                accountId1,
                AccountType.PRIMARY,
                defaultCategoryId1,
                Currency.GBP,
                now1,
                "name");

        Assertions.assertEquals(accountId1, account1.accountUid());
        Assertions.assertEquals(AccountType.PRIMARY, account1.accountType());
        Assertions.assertEquals(defaultCategoryId1, account1.defaultCategory());
        Assertions.assertEquals(Currency.GBP, account1.currency());
        Assertions.assertEquals(now1, account1.createdAt());
        Assertions.assertEquals("name", account1.name());

        final UUID accountId2 = UUID.randomUUID();
        final UUID defaultCategoryId2 = UUID.randomUUID();
        final LocalDateTime now2 = LocalDateTime.now();
        final Account account2 = new Account(
                accountId2,
                AccountType.PRIMARY,
                defaultCategoryId2,
                Currency.GBP,
                now2,
                "name");

        Assertions.assertEquals(accountId2, account2.accountUid());
        Assertions.assertEquals(AccountType.PRIMARY, account2.accountType());
        Assertions.assertEquals(defaultCategoryId2, account2.defaultCategory());
        Assertions.assertEquals(Currency.GBP, account2.currency());
        Assertions.assertEquals(now2, account2.createdAt());
        Assertions.assertEquals("name", account2.name());

        final Accounts accounts = new Accounts(List.of(account1, account2));

        Assertions.assertEquals(accountId1, accounts.accounts().getFirst().accountUid());
        Assertions.assertEquals(AccountType.PRIMARY, accounts.accounts().getFirst().accountType());
        Assertions.assertEquals(defaultCategoryId1, accounts.accounts().getFirst().defaultCategory());
        Assertions.assertEquals(Currency.GBP, accounts.accounts().getFirst().currency());
        Assertions.assertEquals(now1, accounts.accounts().get(0).createdAt());
        Assertions.assertEquals("name", accounts.accounts().get(0).name());

        Assertions.assertEquals(accountId2, accounts.accounts().get(1).accountUid());
        Assertions.assertEquals(AccountType.PRIMARY, accounts.accounts().get(1).accountType());
        Assertions.assertEquals(defaultCategoryId2, accounts.accounts().get(1).defaultCategory());
        Assertions.assertEquals(Currency.GBP, accounts.accounts().get(1).currency());
        Assertions.assertEquals(now2, accounts.accounts().get(1).createdAt());
        Assertions.assertEquals("name", accounts.accounts().get(1).name());
    }

}