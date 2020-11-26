package fr.nextdigital.lab.account.web.action;

import fr.nextdigital.lab.account.web.domain.Account;
import fr.nextdigital.lab.account.web.domain.AccountModule;
import fr.nextdigital.lab.account.web.domain.AccountService;
import fr.nextdigital.lab.account.web.domain.AccountStatus;
import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.account.web.event.AccountEvent;
import fr.nextdigital.lab.account.web.event.AccountEventType;

import static fr.nextdigital.lab.account.web.domain.AccountStatus.ACCOUNT_CONFIRMED;
import static fr.nextdigital.lab.account.web.domain.AccountStatus.ACCOUNT_PENDING;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Confirms an {@link Account}
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class ConfirmAccount extends Action<Account> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    public Account apply(Account account) {
        Assert.isTrue(account.getStatus() != ACCOUNT_CONFIRMED, "The account has already been confirmed");
        Assert.isTrue(account.getStatus() == ACCOUNT_PENDING, "The account has already been confirmed");

        AccountService accountService = account.getModule(AccountModule.class)
                .getDefaultService();

        AccountStatus status = account.getStatus();

        // Activate the account
        account.setStatus(AccountStatus.ACCOUNT_CONFIRMED);
        account = accountService.update(account);

        try {
            // Trigger the account confirmed event
            account.sendAsyncEvent(new AccountEvent(AccountEventType.ACCOUNT_CONFIRMED, account));
        } catch (Exception ex) {
            log.error("Account could not be confirmed", ex);

            // Rollback the operation
            account.setStatus(status);
            account = accountService.update(account);
        }

        return account;
    }
}
