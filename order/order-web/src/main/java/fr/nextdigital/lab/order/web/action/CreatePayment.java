package fr.nextdigital.lab.order.web.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.order.web.domain.Order;
import fr.nextdigital.lab.order.web.domain.OrderModule;
import fr.nextdigital.lab.order.web.domain.OrderService;
import fr.nextdigital.lab.order.web.domain.OrderStatus;
import fr.nextdigital.lab.order.web.event.OrderEvent;
import fr.nextdigital.lab.order.web.event.OrderEventType;
import fr.nextdigital.lab.order.web.payment.domain.Payment;
import fr.nextdigital.lab.order.web.payment.domain.PaymentMethod;
import fr.nextdigital.lab.order.web.payment.domain.PaymentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Creates a {@link Payment} for an {@link Order}.
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class CreatePayment extends Action<Order> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private final PaymentService paymentService;

    public CreatePayment(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public Order apply(Order order) {
        Assert.isTrue(order.getPaymentId() == null, "Payment has already been created");
        Assert.isTrue(!Arrays.asList(OrderStatus.PAYMENT_CREATED,
                OrderStatus.PAYMENT_CONNECTED,
                OrderStatus.PAYMENT_SUCCEEDED,
                OrderStatus.PAYMENT_PENDING).contains(order.getStatus()), "Payment has already been created");
        Assert.isTrue(order.getStatus() == OrderStatus.RESERVATION_SUCCEEDED,
                "Inventory reservations for the order must be made first before creating a payment");

        // Get entity services
        OrderService orderService = order.getModule(OrderModule.class).getDefaultService();

        // Update the order status
        order.setStatus(OrderStatus.PAYMENT_PENDING);
        order = orderService.update(order);

        Payment payment = new Payment();
        payment.setAmount(order.calculateTotal());
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment = paymentService.create(payment);

        // Update the order status
        order.setStatus(OrderStatus.PAYMENT_CREATED);
        order = orderService.update(order);

        try {
            OrderEvent event = new OrderEvent(OrderEventType.PAYMENT_CREATED, order);
            event.add(payment.getLink("self").withRel("payment"));

            // Trigger payment created event
            order.sendAsyncEvent(event);
        } catch (Exception ex) {
            log.error("The order's payment could not be created", ex);

            // Rollback the payment creation
            if (payment.getIdentity() != null)
                paymentService.delete(payment.getIdentity());

            order.setPaymentId(null);
            order.setStatus(OrderStatus.ACCOUNT_CONNECTED);
            order = orderService.update(order);
        }

        return order;
    }
}
