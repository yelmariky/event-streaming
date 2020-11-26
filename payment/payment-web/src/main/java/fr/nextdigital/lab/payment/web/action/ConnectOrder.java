package fr.nextdigital.lab.payment.web.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.payment.web.domain.Payment;
import fr.nextdigital.lab.payment.web.domain.PaymentModule;
import fr.nextdigital.lab.payment.web.domain.PaymentService;
import fr.nextdigital.lab.payment.web.domain.PaymentStatus;
import fr.nextdigital.lab.payment.web.event.PaymentEvent;
import fr.nextdigital.lab.payment.web.event.PaymentEventType;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class ConnectOrder extends Action<Payment> {
    private final Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    public Payment apply(Payment payment, Long orderId) {
        Assert.isTrue(payment
                .getStatus() == PaymentStatus.PAYMENT_CREATED, "Payment has already been connected to an order");

        PaymentService paymentService = payment.getModule(PaymentModule.class)
                .getDefaultService();

        // Connect the payment to the order
        payment.setOrderId(orderId);
        payment.setStatus(PaymentStatus.ORDER_CONNECTED);
        payment = paymentService.update(payment);

        try {
            // Trigger the payment connected
            payment.sendAsyncEvent(new PaymentEvent(PaymentEventType.ORDER_CONNECTED, payment));
        } catch (IllegalStateException ex) {
            log.error("Payment could not be connected to order", ex);

            // Rollback operation
            payment.setStatus(PaymentStatus.PAYMENT_CREATED);
            payment.setOrderId(null);
            payment = paymentService.update(payment);
        }

        return payment;
    }
}
