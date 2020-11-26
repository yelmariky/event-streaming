package fr.nextdigital.lab.account.worker.event;

import fr.nextdigital.lab.account.worker.domain.Account;
import fr.nextdigital.lab.account.worker.domain.AccountStatus;

/**
 * The {@link AccountEventType} represents a collection of possible events that describe
 * state transitions of {@link AccountStatus} on the {@link Account} aggregate.
 *
 * @author kbastani
 */
public enum AccountEventType {
    ACCOUNT_CREATED,
    ACCOUNT_CONFIRMED,
    ACCOUNT_ACTIVATED,
    ACCOUNT_SUSPENDED,
    ACCOUNT_ARCHIVED
}
