package fr.nextdigital.lab.order.worker.function;

import fr.nextdigital.lab.order.worker.domain.Order;
import fr.nextdigital.lab.order.worker.domain.OrderStatus;
import fr.nextdigital.lab.order.worker.event.OrderEvent;
import fr.nextdigital.lab.order.worker.event.OrderEventType;

import org.slf4j.Logger;
import org.springframework.statemachine.StateContext;

import java.util.function.Function;

/**
 * The {@link OrderFunction} is an abstraction used to map actions that are triggered by
 * state transitions on a {@link Order} resource on to a function. Mapped functions
 * can take multiple forms and reside either remotely or locally on the classpath of this application.
 *
 * @author kbastani
 */
public abstract class OrderFunction {

    final private Logger log = org.slf4j.LoggerFactory.getLogger(OrderFunction.class);
    final protected StateContext<OrderStatus, OrderEventType> context;
    final protected Function<OrderEvent, Order> lambda;

    /**
     * Create a new instance of a class that extends {@link OrderFunction}, supplying
     * a state context and a lambda function used to apply {@link OrderEvent} to a provided
     * action.
     *
     * @param context is the {@link StateContext} for a replicated state machine
     * @param lambda  is the lambda function describing an action that consumes an {@link OrderEvent}
     */
    public OrderFunction(StateContext<OrderStatus, OrderEventType> context,
                         Function<OrderEvent, Order> lambda) {
        this.context = context;
        this.lambda = lambda;
    }

    /**
     * Apply an {@link OrderEvent} to the lambda function that was provided through the
     * constructor of this {@link OrderFunction}.
     *
     * @param event is the {@link OrderEvent} to apply to the lambda function
     */
    public Order apply(OrderEvent event) {
        // Execute the lambda function
        Order result = lambda.apply(event);
        context.getExtendedState().getVariables().put("order", result);
        log.info("Order function: " + event.getType());
        return result;
    }
}
