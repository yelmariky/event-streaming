package fr.nextdigital.lab.account.web.action;

import fr.nextdigital.lab.account.web.domain.Account;
import fr.nextdigital.lab.account.web.domain.AccountModule;
import fr.nextdigital.lab.account.web.domain.AccountService;
import fr.nextdigital.lab.account.web.domain.AccountStatus;
import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.account.web.event.AccountEvent;
import fr.nextdigital.lab.account.web.event.AccountEventType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static fr.nextdigital.lab.account.web.domain.AccountStatus.*;

import java.util.Arrays;

/**
 * Activates an {@link Account}
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class ActivateAccount extends Action<Account> {
    private final Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    public Account apply(Account account) {
        Assert.isTrue(account.getStatus() != ACCOUNT_ACTIVE, "The account is already active");
        Assert.isTrue(Arrays.asList(ACCOUNT_CONFIRMED, ACCOUNT_SUSPENDED, ACCOUNT_ARCHIVED)
                .contains(account.getStatus()), "The account cannot be activated");

        AccountService accountService = account.getModule(AccountModule.class)
                .getDefaultService();

        AccountStatus status = account.getStatus();

        // Activate the account
        account.setStatus(AccountStatus.ACCOUNT_ACTIVE);
        account = accountService.update(account);

        try {
            // Trigger the account activated event
            account.sendAsyncEvent(new AccountEvent(AccountEventType.ACCOUNT_ACTIVATED, account));
        } catch (Exception ex) {
            log.error("Account could not be activated", ex);

            // Rollback the operation
            account.setStatus(status);
            account = accountService.update(account);
        }

        return account;
    }
}
