package fr.nextdigital.lab.order.web.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.order.web.domain.Order;
import fr.nextdigital.lab.order.web.domain.OrderModule;
import fr.nextdigital.lab.order.web.payment.domain.Payment;
import fr.nextdigital.lab.order.web.payment.domain.PaymentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Processes a {@link Payment} for an {@link Order}.
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class DeleteOrder extends Action<Order> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private final PaymentService paymentService;

    public DeleteOrder(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void apply(Order order) {
        // Delete payment
        if (order.getPaymentId() != null)
            paymentService.delete(order.getPaymentId());

        // Delete order
        order.getModule(OrderModule.class)
                .getDefaultService()
                .delete(order.getIdentity());
    }
}
