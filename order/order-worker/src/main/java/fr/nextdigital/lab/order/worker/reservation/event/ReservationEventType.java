package fr.nextdigital.lab.order.worker.reservation.event;

import fr.nextdigital.lab.order.worker.reservation.domain.Reservation;
import fr.nextdigital.lab.order.worker.reservation.domain.ReservationStatus;

/**
 * The {@link ReservationEventType} represents a collection of possible events that describe state transitions of
 * {@link ReservationStatus} on the {@link Reservation} aggregate.
 *
 * @author Kenny Bastani
 */
public enum ReservationEventType {
    RESERVATION_CREATED,
    INVENTORY_CONNECTED,
    ORDER_CONNECTED,
    RESERVATION_SUCCEEDED,
    RESERVATION_REQUESTED,
    RESERVATION_FAILED
}
