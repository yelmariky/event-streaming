package fr.nextdigital.lab.order.worker.function;

import fr.nextdigital.lab.order.worker.domain.Order;
import fr.nextdigital.lab.order.worker.domain.OrderStatus;
import fr.nextdigital.lab.order.worker.event.OrderEvent;
import fr.nextdigital.lab.order.worker.event.OrderEventType;

import org.slf4j.Logger;
import org.springframework.statemachine.StateContext;

import java.util.function.Function;

public class PaymentFailed extends OrderFunction {

    final private Logger log = org.slf4j.LoggerFactory.getLogger(PaymentFailed.class);

    public PaymentFailed(StateContext<OrderStatus, OrderEventType> context, Function<OrderEvent, Order> lambda) {
        super(context, lambda);
    }

    /**
     * Apply an {@link OrderEvent} to the lambda function that was provided through the
     * constructor of this {@link OrderFunction}.
     *
     * @param event is the {@link OrderEvent} to apply to the lambda function
     */
    @Override
    public Order apply(OrderEvent event) {
        log.info("Executing workflow for payment failed...");
        return super.apply(event);
    }
}
