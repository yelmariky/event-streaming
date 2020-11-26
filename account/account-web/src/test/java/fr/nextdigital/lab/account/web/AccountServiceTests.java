package fr.nextdigital.lab.account.web;

import fr.nextdigital.lab.account.web.domain.Account;
import fr.nextdigital.lab.account.web.domain.AccountService;
import fr.nextdigital.lab.account.web.domain.AccountStatus;
import fr.nextdigital.lab.account.web.event.AccountEvent;
import fr.nextdigital.lab.account.web.event.AccountEventType;
import fr.nextdigital.lab.event.EventService;
import fr.nextdigital.lab.account.web.repository.AccountRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTests {

    @MockBean
    private EventService<AccountEvent, Long> eventService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private DiscoveryClient discoveryClient;

    @Autowired
    private AccountService accountService;

    @Test
    public void getAccountReturnsAccount() throws Exception {
        Account expected = new Account("Jane", "Doe", "jane.doe@example.com");

        given(this.accountRepository.findById(1L)).willReturn(Optional.of(expected));

        Account actual = accountService.get(1L);

        assertThat(actual).isNotNull();
        assertThat(actual.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(actual.getFirstName()).isEqualTo("Jane");
        assertThat(actual.getLastName()).isEqualTo("Doe");
    }

    @Test
    public void createAccountReturnsAccount() throws Exception {
        Account account = new Account("Jane", "Doe", "jane.doe@example.com");
        account.setIdentity(1L);

        given(this.accountRepository.findById(1L)).willReturn(Optional.of(account));
        given(this.accountRepository.saveAndFlush(account)).willReturn(account);

        Account actual = accountService.create(account);

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(AccountStatus.ACCOUNT_CREATED);
        assertThat(actual.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(actual.getFirstName()).isEqualTo("Jane");
        assertThat(actual.getLastName()).isEqualTo("Doe");
    }

    @Test
    public void applyCommandSuspendsAccount() throws Exception {
        Account account = new Account("Jane", "Doe", "jane.doe@example.com");
        account.setIdentity(1L);
        account.setStatus(AccountStatus.ACCOUNT_ACTIVE);

        AccountEvent accountEvent = new AccountEvent(AccountEventType.ACCOUNT_SUSPENDED);
        accountEvent.setEntity(account);
        accountEvent.setEventId(1L);
        AccountEvent expected = new AccountEvent(AccountEventType.ACCOUNT_SUSPENDED, account);

        given(this.accountRepository.findById(1L)).willReturn(Optional.of(account));
        given(this.accountRepository.existsById(1L)).willReturn(true);
        given(this.accountRepository.save(account)).willReturn(account);
        given(this.eventService.send(expected)).willReturn(accountEvent);

        Account actual = account.suspend();

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(AccountStatus.ACCOUNT_SUSPENDED);
    }

    @Test
    public void applyCommandUnsuspendsAccount() throws Exception {
        Account account = new Account("Jane", "Doe", "jane.doe@example.com");
        account.setIdentity(1L);
        account.setStatus(AccountStatus.ACCOUNT_SUSPENDED);

        AccountEvent accountEvent = new AccountEvent(AccountEventType.ACCOUNT_ACTIVATED);
        accountEvent.setEntity(account);
        accountEvent.setEventId(1L);
        AccountEvent expected = new AccountEvent(AccountEventType.ACCOUNT_ACTIVATED, account);

        given(this.accountRepository.findById(1L)).willReturn(Optional.of(account));
        given(this.accountRepository.existsById(1L)).willReturn(true);
        given(this.accountRepository.save(account)).willReturn(account);
        given(this.eventService.send(expected)).willReturn(accountEvent);

        Account actual = account.activate();

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(AccountStatus.ACCOUNT_ACTIVE);
    }

    @Test
    public void applyCommandArchivesAccount() throws Exception {
        Account account = new Account("Jane", "Doe", "jane.doe@example.com");
        account.setIdentity(1L);
        account.setStatus(AccountStatus.ACCOUNT_ACTIVE);

        AccountEvent accountEvent = new AccountEvent(AccountEventType.ACCOUNT_ARCHIVED);
        accountEvent.setEntity(account);
        accountEvent.setEventId(1L);
        AccountEvent expected = new AccountEvent(AccountEventType.ACCOUNT_ARCHIVED, account);

        given(this.accountRepository.findById(1L)).willReturn(Optional.of(account));
        given(this.accountRepository.existsById(1L)).willReturn(true);
        given(this.accountRepository.save(account)).willReturn(account);
        given(this.eventService.send(expected)).willReturn(accountEvent);

        Account actual = account.archive();

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(AccountStatus.ACCOUNT_ARCHIVED);
    }

    @Test
    public void applyCommandUnarchivesAccount() throws Exception {
        Account account = new Account("Jane", "Doe", "jane.doe@example.com");
        account.setIdentity(1L);
        account.setStatus(AccountStatus.ACCOUNT_ARCHIVED);

        AccountEvent accountEvent = new AccountEvent(AccountEventType.ACCOUNT_ACTIVATED);
        accountEvent.setEntity(account);
        accountEvent.setEventId(1L);
        AccountEvent expected = new AccountEvent(AccountEventType.ACCOUNT_ACTIVATED, account);

        given(this.accountRepository.findById(1L)).willReturn(Optional.of(account));
        given(this.accountRepository.existsById(1L)).willReturn(true);
        given(this.accountRepository.save(account)).willReturn(account);
        given(this.eventService.send(expected)).willReturn(accountEvent);

        Account actual = account.activate();

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(AccountStatus.ACCOUNT_ACTIVE);
    }

    @Test
    public void applyCommandConfirmsAccount() throws Exception {
        Account account = new Account("Jane", "Doe", "jane.doe@example.com");
        account.setIdentity(1L);
        account.setStatus(AccountStatus.ACCOUNT_PENDING);

        AccountEvent accountEvent = new AccountEvent(AccountEventType.ACCOUNT_CONFIRMED);
        accountEvent.setEntity(account);
        accountEvent.setEventId(1L);
        AccountEvent expected = new AccountEvent(AccountEventType.ACCOUNT_CONFIRMED, account);

        given(this.accountRepository.findById(1L)).willReturn(Optional.of(account));
        given(this.accountRepository.existsById(1L)).willReturn(true);
        given(this.accountRepository.save(account)).willReturn(account);
        given(this.eventService.send(expected)).willReturn(accountEvent);

        Account actual = account.confirm();

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(AccountStatus.ACCOUNT_CONFIRMED);
    }

    @Test
    public void applyCommandActivatesAccount() throws Exception {
        Account account = new Account("Jane", "Doe", "jane.doe@example.com");
        account.setIdentity(1L);
        account.setStatus(AccountStatus.ACCOUNT_CONFIRMED);

        AccountEvent accountEvent = new AccountEvent(AccountEventType.ACCOUNT_ACTIVATED);
        accountEvent.setEntity(account);
        accountEvent.setEventId(1L);
        AccountEvent expected = new AccountEvent(AccountEventType.ACCOUNT_ACTIVATED, account);

        given(this.accountRepository.findById(1L)).willReturn(Optional.of(account));
        given(this.accountRepository.existsById(1L)).willReturn(true);
        given(this.accountRepository.save(account)).willReturn(account);
        given(this.eventService.send(expected)).willReturn(accountEvent);

        Account actual = account.activate();

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(AccountStatus.ACCOUNT_ACTIVE);
    }
}