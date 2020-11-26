package fr.nextdigital.lab.warehouse.web.reservation.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.warehouse.web.reservation.domain.Reservation;
import fr.nextdigital.lab.warehouse.web.reservation.domain.ReservationModule;
import fr.nextdigital.lab.warehouse.web.reservation.domain.ReservationService;
import fr.nextdigital.lab.warehouse.web.reservation.domain.ReservationStatus;
import fr.nextdigital.lab.warehouse.web.reservation.event.ReservationEvent;
import fr.nextdigital.lab.warehouse.web.reservation.event.ReservationEventType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Connects an {@link Reservation} to an Order.
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class ConnectOrder extends Action<Reservation> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Reservation apply(Reservation reservation, Long orderId) {
        Assert.isTrue(reservation
                .getStatus() == ReservationStatus.RESERVATION_CREATED, "Reservation must be in a created state");

        ReservationService reservationService = reservation.getModule(ReservationModule.class).getDefaultService();

        // Connect the order
        reservation.setOrderId(orderId);
        reservation.setStatus(ReservationStatus.ORDER_CONNECTED);
        reservation = reservationService.update(reservation);

        try {
            // Trigger the order connected event
            reservation.sendAsyncEvent(new ReservationEvent(ReservationEventType.ORDER_CONNECTED, reservation));
        } catch (Exception ex) {
            log.error("Could not connect reservation to order", ex);
            reservation.setOrderId(null);
            reservation.setStatus(ReservationStatus.RESERVATION_CREATED);
            reservation = reservationService.update(reservation);
        }

        return reservation;
    }
}
