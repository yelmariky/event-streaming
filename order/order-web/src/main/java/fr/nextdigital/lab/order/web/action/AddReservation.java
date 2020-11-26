package fr.nextdigital.lab.order.web.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.order.web.domain.Order;
import fr.nextdigital.lab.order.web.domain.OrderModule;
import fr.nextdigital.lab.order.web.domain.OrderService;
import fr.nextdigital.lab.order.web.domain.OrderStatus;
import fr.nextdigital.lab.order.web.event.OrderEvent;
import fr.nextdigital.lab.order.web.event.OrderEventType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Connects an {@link Order} to an Account.
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class AddReservation extends Action<Order> {
    private final Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    public Order apply(Order order, Long reservationId) {
        Assert.isTrue(order
                .getStatus() == OrderStatus.RESERVATION_PENDING, "Order must be in a pending reservation state");
        Assert.isTrue(!order.getReservationIds().contains(reservationId), "Reservation already added to order");

        OrderService orderService = order.getModule(OrderModule.class).getDefaultService();

        order.getReservationIds().add(reservationId);
        order = orderService.update(order);

        Link reservationLink = new Link(new UriTemplate("http://warehouse-web/v1/reservations/{id}")
                .with("id", TemplateVariable.VariableType.PATH_VARIABLE)
                .expand(reservationId)
                .toString()).withRel("reservation");

        try {
            // Trigger reservation added event
            order.sendAsyncEvent(new OrderEvent(OrderEventType.RESERVATION_ADDED, order), reservationLink);
        } catch (Exception ex) {
            log.error("Could not add reservation to order", ex);
            order.getReservationIds().remove(reservationId);
            order.setStatus(OrderStatus.RESERVATION_FAILED);
            order = orderService.update(order);
            order.sendAsyncEvent(new OrderEvent(OrderEventType.RESERVATION_FAILED, order), reservationLink);
        }

        return order;
    }

}
