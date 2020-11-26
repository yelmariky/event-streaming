package fr.nextdigital.lab.account.web.domain;

import fr.nextdigital.lab.domain.Module;
import fr.nextdigital.lab.account.web.event.AccountEvent;
import fr.nextdigital.lab.event.EventService;

@org.springframework.stereotype.Service
public class AccountModule extends Module<Account> {

    private final AccountService accountService;
    private final EventService<AccountEvent, Long> eventService;

    public AccountModule(AccountService accountService, EventService<AccountEvent, Long> eventService) {
        this.accountService = accountService;
        this.eventService = eventService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public EventService<AccountEvent, Long> getEventService() {
        return eventService;
    }

    @Override
    public AccountService getDefaultService() {
        return accountService;
    }

    @Override
    public EventService<AccountEvent, Long> getDefaultEventService() {
        return eventService;
    }
}
