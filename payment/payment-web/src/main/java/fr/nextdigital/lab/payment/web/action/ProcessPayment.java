package fr.nextdigital.lab.payment.web.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.payment.web.domain.Payment;
import fr.nextdigital.lab.payment.web.domain.PaymentService;
import fr.nextdigital.lab.payment.web.domain.PaymentStatus;
import fr.nextdigital.lab.payment.web.event.PaymentEvent;
import fr.nextdigital.lab.payment.web.event.PaymentEventType;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static fr.nextdigital.lab.payment.web.domain.PaymentStatus.PAYMENT_FAILED;
import static fr.nextdigital.lab.payment.web.domain.PaymentStatus.PAYMENT_SUCCEEDED;

import java.util.Arrays;

@Service
@Transactional
public class ProcessPayment extends Action<Payment> {
    private final Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private final PaymentService paymentService;

    public ProcessPayment(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public Payment apply(Payment payment) {
        // Validations
        Assert.isTrue(!Arrays.asList(PAYMENT_SUCCEEDED,
                PaymentStatus.PAYMENT_PENDING,
                PaymentStatus.PAYMENT_FAILED).contains(payment.getStatus()), "Payment has already been processed");
        Assert.isTrue(payment.getStatus() == PaymentStatus.ORDER_CONNECTED,
                "Payment must be connected to an order");

        payment.setStatus(PaymentStatus.PAYMENT_PROCESSED);
        payment = paymentService.update(payment);

        try {
            // Trigger the payment processed event
            payment.sendAsyncEvent(new PaymentEvent(PaymentEventType.PAYMENT_PROCESSED, payment));
        } catch (Exception ex) {
            log.error("Payment could not be processed", ex);
            payment = finalizePayment(payment, PAYMENT_FAILED);
        } finally {
            if(payment.getStatus() != PAYMENT_FAILED) {
                // Handle the result asynchronously
                payment = finalizePayment(payment, PAYMENT_SUCCEEDED);
            }
        }

        return payment;
    }

    private Payment finalizePayment(Payment payment, PaymentStatus paymentStatus) {
        payment = paymentService.get(payment.getIdentity());
        payment.setStatus(paymentStatus);
        payment.sendAsyncEvent(new PaymentEvent(PaymentEventType.valueOf(paymentStatus.toString()), payment));
        return payment;
    }
}
