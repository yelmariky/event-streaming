package fr.nextdigital.lab.payment.worker.event;

import fr.nextdigital.lab.payment.worker.payment.Payment;
import fr.nextdigital.lab.payment.worker.payment.PaymentStatus;

/**
 * The {@link PaymentEventType} represents a collection of possible events that describe
 * state transitions of {@link PaymentStatus} on the {@link Payment} aggregate.
 *
 * @author kbastani
 */
public enum PaymentEventType {
    PAYMENT_CREATED,
    ORDER_CONNECTED,
    PAYMENT_PENDING,
    PAYMENT_PROCESSED,
    PAYMENT_FAILED,
    PAYMENT_SUCCEEDED
}
