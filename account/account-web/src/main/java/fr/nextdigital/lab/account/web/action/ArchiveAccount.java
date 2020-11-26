package fr.nextdigital.lab.account.web.action;

import fr.nextdigital.lab.account.web.domain.Account;
import fr.nextdigital.lab.account.web.domain.AccountModule;
import fr.nextdigital.lab.account.web.domain.AccountService;
import fr.nextdigital.lab.account.web.domain.AccountStatus;
import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.account.web.event.AccountEvent;
import fr.nextdigital.lab.account.web.event.AccountEventType;

import static fr.nextdigital.lab.account.web.domain.AccountStatus.ACCOUNT_ACTIVE;
import static fr.nextdigital.lab.account.web.domain.AccountStatus.ACCOUNT_ARCHIVED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Archives an {@link Account}
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class ArchiveAccount extends Action<Account> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    public Account apply(Account account) {
        Assert.isTrue(account.getStatus() != ACCOUNT_ARCHIVED, "The account is already archived");
        Assert.isTrue(account.getStatus() == ACCOUNT_ACTIVE, "An inactive account cannot be archived");

        AccountService accountService = account.getModule(AccountModule.class)
                .getDefaultService();

        AccountStatus status = account.getStatus();

        // Archive the account
        account.setStatus(AccountStatus.ACCOUNT_ARCHIVED);
        account = accountService.update(account);

        try {
            // Trigger the account archived event
            account.sendAsyncEvent(new AccountEvent(AccountEventType.ACCOUNT_ARCHIVED, account));
        } catch (Exception ex) {
            log.error("Account could not be archived", ex);

            // Rollback the operation
            account.setStatus(status);
            account = accountService.update(account);
        }

        return account;
    }
}
