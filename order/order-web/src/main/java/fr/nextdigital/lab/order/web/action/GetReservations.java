package fr.nextdigital.lab.order.web.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.order.web.domain.Order;
import fr.nextdigital.lab.order.web.reservation.domain.ReservationModule;
import fr.nextdigital.lab.order.web.reservation.domain.Reservations;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Query action to get {@link fr.nextdigital.lab.order.web.domain.Order}s for an an {@link Order}
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class GetReservations extends Action<Order> {

    private final ReservationModule reservationModule;

    public GetReservations(ReservationModule reservationModule) {
        this.reservationModule = reservationModule;
    }

    public Reservations apply(Order order) {
        // Get orders from the order service
        return reservationModule.getDefaultService()
                .findReservationsByOrderId(order.getIdentity());
    }
}
