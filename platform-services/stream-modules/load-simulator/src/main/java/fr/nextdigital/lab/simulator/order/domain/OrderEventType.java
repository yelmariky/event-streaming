package fr.nextdigital.lab.simulator.order.domain;

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
    PAYMENT_FAILED
}
