package fr.nextdigital.lab.account.web.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.nextdigital.lab.account.web.controller.AccountController;
import fr.nextdigital.lab.account.web.domain.Account;
import fr.nextdigital.lab.event.Event;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.Link;

import javax.persistence.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * The domain event {@link AccountEvent} tracks the type and state of events as applied to the {@link Account} domain
 * object. This event resource can be used to event source the aggregate state of {@link Account}.
 * <p>
 * This event resource also provides a transaction log that can be used to append actions to the event.
 *
 * @author Kenny Bastani
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = { @Index(name = "IDX_ACCOUNT_EVENT", columnList = "entity_id") })
public class AccountEvent extends Event<Account, AccountEventType, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;

    @Enumerated(EnumType.STRING)
    private AccountEventType type;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnore
    private Account entity;

    @CreatedDate
    private Long createdAt;

    @LastModifiedDate
    private Long lastModified;

    public AccountEvent() {
    }

    public AccountEvent(AccountEventType type) {
        this.type = type;
    }

    public AccountEvent(AccountEventType type, Account entity) {
        this.type = type;
        this.entity = entity;
    }

    @Override
    public Long getEventId() {
        return eventId;
    }

    @Override
    public void setEventId(Long id) {
        eventId = id;
    }

    @Override
    public AccountEventType getType() {
        return type;
    }

    @Override
    public void setType(AccountEventType type) {
        this.type = type;
    }

    @Override
    public Account getEntity() {
        return entity;
    }

    @Override
    public void setEntity(Account entity) {
        this.entity = entity;
    }

    @Override
    public Long getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Long getLastModified() {
        return lastModified;
    }

    @Override
    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public Link getId() {
        return linkTo(AccountController.class).slash("accounts").slash(getEntity().getIdentity()).slash("events")
                .slash(getEventId()).withSelfRel();
    }
    
    @Override
    public String toString() {
        return "AccountEvent{" +
                "eventId=" + eventId +
                ", type=" + type +
                ", entity=" + entity +
                ", createdAt=" + createdAt +
                ", lastModified=" + lastModified +
                "} " + super.toString();
    }
}
