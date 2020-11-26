package fr.nextdigital.lab.payment.worker.payment;

/**
 * The {@link PaymentStatus} describes the state of an {@link Payment}.
 * The aggregate state of a {@link Payment} is sourced from attached domain
 * events in the form of {@link fr.nextdigital.lab.payment.worker.event.PaymentEvent}.
 *
 * @author kbastani
 */
public enum PaymentStatus {
    PAYMENT_CREATED,
    ORDER_CONNECTED,
    PAYMENT_PENDING,
    PAYMENT_PROCESSED,
    PAYMENT_FAILED,
    PAYMENT_SUCCEEDED
}
