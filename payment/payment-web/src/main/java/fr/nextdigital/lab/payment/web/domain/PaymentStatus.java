package fr.nextdigital.lab.payment.web.domain;

import fr.nextdigital.lab.payment.web.event.PaymentEvent;

/**
 * The {@link PaymentStatus} describes the state of an {@link Payment}. The aggregate state of a {@link Payment} is
 * sourced from attached domain events in the form of {@link PaymentEvent}.
 *
 * @author Kenny Bastani
 */
public enum PaymentStatus {
    PAYMENT_CREATED,
    ORDER_CONNECTED,
    PAYMENT_PENDING,
    PAYMENT_PROCESSED,
    PAYMENT_FAILED,
    PAYMENT_SUCCEEDED
}
