package fr.nextdigital.lab.order.worker.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.nextdigital.lab.order.worker.domain.AbstractEntity;

import org.springframework.hateoas.Link;

public class OrderEvent extends AbstractEntity {

    private OrderEventType type;

    public OrderEvent() {
    }

    public OrderEvent(OrderEventType type) {
        this.type = type;
    }

    public OrderEventType getType() {
        return type;
    }

    public void setType(OrderEventType type) {
        this.type = type;
    }

    @JsonIgnore
    @Override
    public Link getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "type=" + type +
                "} " + super.toString();
    }
}
