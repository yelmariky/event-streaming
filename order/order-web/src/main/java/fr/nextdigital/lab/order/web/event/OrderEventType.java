package fr.nextdigital.lab.order.web.event;

import fr.nextdigital.lab.order.web.domain.Order;
import fr.nextdigital.lab.order.web.domain.OrderStatus;

/**
 * The {@link OrderEventType} represents a collection of possible events that describe state transitions of
 * {@link OrderStatus} on the {@link Order} aggregate.
 *
 * @author Kenny Bastani
 */
public enum OrderEventType {
    ORDER_CREATED,
    ACCOUNT_CONNECTED,
    RESERVATION_PENDING,
    INVENTORY_RESERVED,
    RESERVATION_SUCCEEDED,
    RESERVATION_FAILED,
    PAYMENT_CREATED,
    PAYMENT_CONNECTED,
    PAYMENT_PENDING,
    PAYMENT_SUCCEEDED,
    ORDER_SUCCEEDED, ORDER_FAILED, RESERVATION_ADDED, PAYMENT_FAILED
}
