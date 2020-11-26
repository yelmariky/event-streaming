package fr.nextdigital.lab.payment.worker.function;

import fr.nextdigital.lab.payment.worker.event.PaymentEvent;
import fr.nextdigital.lab.payment.worker.event.PaymentEventType;
import fr.nextdigital.lab.payment.worker.payment.Payment;
import fr.nextdigital.lab.payment.worker.payment.PaymentStatus;

import org.slf4j.Logger;
import org.springframework.statemachine.StateContext;

import java.util.function.Function;

public class PaymentProcessed extends PaymentFunction {

    final private Logger log = org.slf4j.LoggerFactory.getLogger(PaymentProcessed.class);

    public PaymentProcessed(StateContext<PaymentStatus, PaymentEventType> context, Function<PaymentEvent, Payment> lambda) {
        super(context, lambda);
    }

    /**
     * Apply an {@link PaymentEvent} to the lambda function that was provided through the
     * constructor of this {@link PaymentFunction}.
     *
     * @param event is the {@link PaymentEvent} to apply to the lambda function
     */
    @Override
    public Payment apply(PaymentEvent event) {
        log.info("Executing workflow for payment processed...");
        return super.apply(event);
    }
}
