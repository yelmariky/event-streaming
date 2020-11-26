package fr.nextdigital.lab.account.worker.function;

import fr.nextdigital.lab.account.worker.domain.Account;
import fr.nextdigital.lab.account.worker.domain.AccountStatus;
import fr.nextdigital.lab.account.worker.event.AccountEvent;
import fr.nextdigital.lab.account.worker.event.AccountEventType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;

import java.util.function.Function;

/**
 * The {@link AccountFunction} is an abstraction used to map actions that are triggered by
 * state transitions on a {@link fr.nextdigital.lab.account.worker.domain.Account} resource on to a function. Mapped functions
 * can take multiple forms and reside either remotely or locally on the classpath of this application.
 *
 * @author kbastani
 */
public class UnsuspendAccount extends AccountFunction {

    final private Logger log = org.slf4j.LoggerFactory.getLogger(UnsuspendAccount.class);

    public UnsuspendAccount(StateContext<AccountStatus, AccountEventType> context, Function<AccountEvent, Account> lambda) {
        super(context, lambda);
    }

    /**
     * Apply an {@link AccountEvent} to the lambda function that was provided through the
     * constructor of this {@link AccountFunction}.
     *
     * @param event is the {@link AccountEvent} to apply to the lambda function
     */
    @Override
    public Account apply(AccountEvent event) {
        log.info("Executing workflow for a unsuspended account...");
        return super.apply(event);
    }
}
