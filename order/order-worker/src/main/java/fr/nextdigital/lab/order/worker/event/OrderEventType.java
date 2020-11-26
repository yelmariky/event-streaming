package fr.nextdigital.lab.order.worker.event;

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
