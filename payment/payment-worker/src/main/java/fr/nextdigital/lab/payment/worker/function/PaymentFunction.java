package fr.nextdigital.lab.payment.worker.function;

import fr.nextdigital.lab.payment.worker.event.PaymentEvent;
import fr.nextdigital.lab.payment.worker.event.PaymentEventType;
import fr.nextdigital.lab.payment.worker.payment.Payment;
import fr.nextdigital.lab.payment.worker.payment.PaymentStatus;

import org.slf4j.Logger;
import org.springframework.statemachine.StateContext;

import java.util.function.Function;

/**
 * The {@link PaymentFunction} is an abstraction used to map actions that are triggered by
 * state transitions on a {@link fr.nextdigital.lab.payment.worker.payment.Payment} resource on to a function. Mapped functions
 * can take multiple forms and reside either remotely or locally on the classpath of this application.
 *
 * @author kbastani
 */
public abstract class PaymentFunction {

    final private Logger log = org.slf4j.LoggerFactory.getLogger(PaymentFunction.class);
    final protected StateContext<PaymentStatus, PaymentEventType> context;
    final protected Function<PaymentEvent, Payment> lambda;

    /**
     * Create a new instance of a class that extends {@link PaymentFunction}, supplying
     * a state context and a lambda function used to apply {@link PaymentEvent} to a provided
     * action.
     *
     * @param context is the {@link StateContext} for a replicated state machine
     * @param lambda  is the lambda function describing an action that consumes an {@link PaymentEvent}
     */
    public PaymentFunction(StateContext<PaymentStatus, PaymentEventType> context,
                           Function<PaymentEvent, Payment> lambda) {
        this.context = context;
        this.lambda = lambda;
    }

    /**
     * Apply an {@link PaymentEvent} to the lambda function that was provided through the
     * constructor of this {@link PaymentFunction}.
     *
     * @param event is the {@link PaymentEvent} to apply to the lambda function
     */
    public Payment apply(PaymentEvent event) {
        // Execute the lambda function
        Payment result = lambda.apply(event);
        context.getExtendedState().getVariables().put("payment", result);
        return result;
    }
}
