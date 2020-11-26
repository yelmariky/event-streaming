package fr.nextdigital.lab.warehouse.worker.order.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.nextdigital.lab.warehouse.worker.domain.AbstractEntity;
import fr.nextdigital.lab.warehouse.worker.order.event.OrderEvent;
import org.springframework.hateoas.Link;

import java.util.HashSet;
import java.util.Set;

public class Order extends AbstractEntity {

    private Long id;
    private Long accountId;
    private Long paymentId;
    private OrderStatus status;
    private Set<OrderEvent> events = new HashSet<>();
    private Set<LineItem> lineItems = new HashSet<>();
    private Address shippingAddress;

    public Order() {
    }

    @JsonProperty("orderId")
    public Long getIdentity() {
        return this.id;
    }

    public void setIdentity(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Set<OrderEvent> getEvents() {
        return events;
    }

    public void setEvents(Set<OrderEvent> events) {
        this.events = events;
    }

    public Set<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Set<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void addLineItem(LineItem lineItem) {
        lineItems.add(lineItem);
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * Returns the {@link Link} with a rel of {@link Link#REL_SELF}.
     */
    @Override
    public Link getId() {
        return getLink("self");
    }
}
