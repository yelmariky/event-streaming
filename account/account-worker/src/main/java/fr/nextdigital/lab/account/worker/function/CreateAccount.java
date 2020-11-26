package fr.nextdigital.lab.account.worker.function;

import fr.nextdigital.lab.account.worker.domain.Account;
import fr.nextdigital.lab.account.worker.domain.AccountStatus;
import fr.nextdigital.lab.account.worker.event.AccountEvent;
import fr.nextdigital.lab.account.worker.event.AccountEventType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.RequestEntity;
import org.springframework.statemachine.StateContext;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.function.Function;

/**
 * The {@link AccountFunction} is an abstraction used to map actions that are triggered by
 * state transitions on a {@link fr.nextdigital.lab.account.worker.domain.Account} resource on to a function. Mapped functions
 * can take multiple forms and reside either remotely or locally on the classpath of this application.
 *
 * @author kbastani
 */
public class CreateAccount extends AccountFunction {

    final private Logger log = org.slf4j.LoggerFactory.getLogger(CreateAccount.class);

    public CreateAccount(StateContext<AccountStatus, AccountEventType> context) {
        this(context, null);
    }

    public CreateAccount(StateContext<AccountStatus, AccountEventType> context,
                         Function<AccountEvent, Account> function) {
        super(context, function);
    }

    /**
     * Applies the {@link AccountEvent} to the {@link Account} aggregate.
     *
     * @param event is the {@link AccountEvent} for this context
     */
    @Override
    public Account apply(AccountEvent event) {

        Account account;

        log.info("Executing workflow for a created account...");
        String linkHref=event.getLink("account").getHref();
        String newLinkToPod="http://account-web.default.svc.cluster.local:80/"+linkHref.substring(linkHref.indexOf("v1"),linkHref.length());

        // Create a traverson for the root account
        Traverson traverson = new Traverson(
                URI.create(newLinkToPod),
                MediaTypes.HAL_JSON
        );

        // Get the account resource attached to the event
        account = traverson.follow("self")
                .toEntity(Account.class)
                .getBody();

        // Set the account to a pending state
        account = setAccountPendingStatus(event, account);

        // The account can only be confirmed if it is in a pending state
        if (account.getStatus() == AccountStatus.ACCOUNT_PENDING) {
            // Traverse to the confirm account command
            account = traverson.follow("commands")
                    .follow("confirm")
                    .toEntity(Account.class)
                    .getBody();

            log.info(event.getType() + ": " +
                    newLinkToPod);
        }

        context.getExtendedState().getVariables().put("account", account);

        return account;
    }

    /**
     * Set the {@link Account} resource to a pending state.
     *
     * @param event   is the {@link AccountEvent} for this context
     * @param account is the {@link Account} attached to the {@link AccountEvent} resource
     * @return an {@link Account} with its updated state set to pending
     */
    private Account setAccountPendingStatus(AccountEvent event, Account account) {
        // Set the account status to pending
        account.setStatus(AccountStatus.ACCOUNT_PENDING);
        RestTemplate restTemplate = new RestTemplate();

        // Create a new request entity
        // Create a new request entity
        String linkHref=event.getLink("account").getHref();
        String newLinkToPod="http://account-web.default.svc.cluster.local:80/"+linkHref.substring(linkHref.indexOf("v1"),linkHref.length());

        RequestEntity<Account> requestEntity = RequestEntity.put(
                URI.create(newLinkToPod))
                .contentType(MediaTypes.HAL_JSON)
                .body(account);

        // Update the account entity's status
        account = restTemplate.exchange(requestEntity, Account.class).getBody();

        return account;
    }
}
