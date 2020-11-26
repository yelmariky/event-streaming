package fr.nextdigital.lab.order.web.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.order.web.domain.Order;
import fr.nextdigital.lab.order.web.domain.OrderModule;
import fr.nextdigital.lab.order.web.domain.OrderService;
import fr.nextdigital.lab.order.web.domain.OrderStatus;
import fr.nextdigital.lab.order.web.event.OrderEvent;
import fr.nextdigital.lab.order.web.event.OrderEventType;
import fr.nextdigital.lab.order.web.payment.domain.Payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.net.URI;
import java.util.Arrays;

/**
 * Processes a {@link Payment} for an {@link Order}.
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class ProcessPayment extends Action<Order> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    public Order apply(Order order) {
        Assert.isTrue(!Arrays
                .asList(OrderStatus.PAYMENT_SUCCEEDED, OrderStatus.PAYMENT_PENDING, OrderStatus.PAYMENT_FAILED)
                .contains(order.getStatus()), "Payment has already been processed");
        Assert.isTrue(order.getStatus() == OrderStatus.PAYMENT_CONNECTED,
                "Order must be in a payment connected state");

        // Get entity services
        OrderService orderService = order.getModule(OrderModule.class).getDefaultService();

        // Get the payment
        Payment payment = order.getPayment();

        // Update the order status
        order.setStatus(OrderStatus.PAYMENT_PENDING);
        order = orderService.update(order);

        boolean paymentSuccess = false;
        String linkHref=payment.getLink("self").getHref();
        String newLinkToPod="http://payment-web.default.svc.cluster.local:80/"+linkHref.substring(linkHref.indexOf("v1"),linkHref.length());

        try {
            // Create traverson for the new order
            Traverson traverson = new Traverson(URI.create(newLinkToPod), MediaTypes.HAL_JSON);
            payment = traverson.follow("commands", "processPayment").toObject(Payment.class);
            paymentSuccess = true;
        } catch (Exception ex) {
            log.error("The order's payment could not be processed", ex);

            OrderEvent event = new OrderEvent(OrderEventType.PAYMENT_FAILED, order);
            event.add(payment.getLink("self").withRel("payment"));

            // Trigger payment failed event
            order.sendAsyncEvent(event);

            paymentSuccess = false;
        } finally {
            if(paymentSuccess) {
                OrderEvent event = new OrderEvent(OrderEventType.PAYMENT_SUCCEEDED, order);
                event.add(payment.getLink("self").withRel("payment"));

                // Trigger payment succeeded event
                order.sendAsyncEvent(event);
            }
        }

        return order;
    }
}
