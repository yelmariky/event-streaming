package fr.nextdigital.lab.account.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.nextdigital.lab.account.web.action.*;
import fr.nextdigital.lab.account.web.controller.AccountController;
import fr.nextdigital.lab.domain.Aggregate;
import fr.nextdigital.lab.domain.Command;
import fr.nextdigital.lab.domain.Module;
import fr.nextdigital.lab.account.web.event.AccountEvent;
import fr.nextdigital.lab.account.web.order.domain.Order;
import fr.nextdigital.lab.account.web.order.domain.Orders;

import org.springframework.hateoas.Link;

import javax.persistence.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Entity
public class Account extends AbstractEntity<AccountEvent, Long> {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus status;

    public Account() {
        status = AccountStatus.ACCOUNT_CREATED;
    }

    public Account(String firstName, String lastName, String email) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @JsonProperty("accountId")
    @Override
    public Long getIdentity() {
        return this.id;
    }

    @Override
    public void setIdentity(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    @JsonIgnore
    public Orders getOrders() {
        return getAction(GetOrders.class)
                .apply(this);
    }

    @Command(method = "activate", controller = AccountController.class)
    public Account activate() {
        return getAction(ActivateAccount.class)
                .apply(this);
    }

    @Command(method = "archive", controller = AccountController.class)
    public Account archive() {
        return getAction(ArchiveAccount.class)
                .apply(this);
    }

    @Command(method = "confirm", controller = AccountController.class)
    public Account confirm() {
        return getAction(ConfirmAccount.class)
                .apply(this);
    }

    @Command(method = "suspend", controller = AccountController.class)
    public Account suspend() {
        return getAction(SuspendAccount.class)
                .apply(this);
    }

    @Command(method = "postOrder", controller = AccountController.class)
    public Order postOrder(Order order) {
        return getAction(PostOrder.class)
                .apply(this, order);
    }

    /**
     * Retrieves an instance of the {@link Module} for this instance
     *
     * @return the provider for this instance
     * @throws IllegalArgumentException if the application context is unavailable or the provider does not exist
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Module<A>, A extends Aggregate<AccountEvent, Long>> T getModule() throws
            IllegalArgumentException {
        AccountModule accountProvider = getModule(AccountModule.class);
        return (T) accountProvider;
    }

    /**
     * Returns the {@link Link} with a rel of {@link Link#REL_SELF}.
     */
    @Override
    public Link getId() {
        return linkTo(AccountController.class)
                .slash("accounts")
                .slash(getIdentity())
                .withSelfRel();
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                "} " + super.toString();
    }
}
