package fr.nextdigital.lab.order.web.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.nextdigital.lab.order.web.controller.OrderController;
import fr.nextdigital.lab.order.web.domain.Order;
import fr.nextdigital.lab.event.Event;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.Link;

import javax.persistence.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * The domain event {@link OrderEvent} tracks the type and state of events as applied to the {@link Order} domain
 * object. This event resource can be used to event source the aggregate state of {@link Order}.
 * <p>
 * This event resource also provides a transaction log that can be used to append actions to the event.
 *
 * @author Kenny Bastani
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "IDX_ORDER_EVENT", columnList = "entity_id")})
public class OrderEvent extends Event<Order, OrderEventType, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;

    @Enumerated(EnumType.STRING)
    private OrderEventType type;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnore
    private Order entity;

    @CreatedDate
    private Long createdAt;

    @LastModifiedDate
    private Long lastModified;

    public OrderEvent() {
    }

    public OrderEvent(OrderEventType type) {
        this.type = type;
    }

    public OrderEvent(OrderEventType type, Order entity) {
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
    public OrderEventType getType() {
        return type;
    }

    @Override
    public void setType(OrderEventType type) {
        this.type = type;
    }

    @Override
    public Order getEntity() {
        return entity;
    }

    @Override
    public void setEntity(Order entity) {
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
        return linkTo(OrderController.class).slash("orders").slash(getEntity().getIdentity()).slash("events")
                .slash(getEventId()).withSelfRel();
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "eventId=" + eventId +
                ", type=" + type +
                ", entity=" + entity +
                ", createdAt=" + createdAt +
                ", lastModified=" + lastModified +
                "} " + super.toString();
    }
}
