package fr.nextdigital.lab.order.web.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.order.web.domain.Order;
import fr.nextdigital.lab.order.web.domain.OrderModule;
import fr.nextdigital.lab.order.web.domain.OrderService;
import fr.nextdigital.lab.order.web.domain.OrderStatus;
import fr.nextdigital.lab.order.web.event.OrderEvent;
import fr.nextdigital.lab.order.web.event.OrderEventType;
import fr.nextdigital.lab.order.web.reservation.domain.Reservation;
import fr.nextdigital.lab.order.web.reservation.domain.ReservationStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static fr.nextdigital.lab.order.web.domain.OrderStatus.RESERVATION_FAILED;
import static fr.nextdigital.lab.order.web.domain.OrderStatus.RESERVATION_SUCCEEDED;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Reserves inventory for an {@link Order}.
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class CompleteReservation extends Action<Order> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(CompleteReservation.class);

    public Order apply(Order order) {
        if (order.getStatus() != RESERVATION_SUCCEEDED && order.getStatus() != RESERVATION_FAILED) {
            Assert.isTrue(order.getStatus() == OrderStatus.RESERVATION_PENDING,
                    "The order must be in a reservation pending state");
        } else {
            // Reservation has already completed
            return order;
        }

        OrderService orderService = order.getModule(OrderModule.class).getDefaultService();

        OrderStatus status = order.getStatus();

        try {
            List<Reservation> reservations = order.getReservations().getContent().stream()
                    .collect(Collectors.toList());

            // Check if all inventory has been reserved
            Boolean orderReserved = reservations.stream()
                    .allMatch(r -> r.getStatus() == ReservationStatus.RESERVATION_SUCCEEDED);

            // Check if any inventory reservations have failed
            Boolean reservationFailed = reservations.stream()
                    .anyMatch(r -> r.getStatus() == ReservationStatus.RESERVATION_FAILED);

            if (orderReserved && order.getStatus() == OrderStatus.RESERVATION_PENDING) {
                // Succeed the reservation and commit all inventory associated with order
                order.setStatus(RESERVATION_SUCCEEDED);
                order = orderService.update(order);
                order.sendAsyncEvent(new OrderEvent(OrderEventType.RESERVATION_SUCCEEDED, order));
            } else if (reservationFailed && order.getStatus() == OrderStatus.RESERVATION_PENDING) {
                // Fail the reservation and release all inventory associated with order
                order.setStatus(RESERVATION_FAILED);
                order = orderService.update(order);
                order.sendAsyncEvent(new OrderEvent(OrderEventType.RESERVATION_FAILED, order));
            }
        } catch (RuntimeException ex) {
            log.error("Error completing reservation", ex);
            // Rollback status change
            order.setStatus(status);
            order = orderService.update(order);
        }

        return order;
    }
}
