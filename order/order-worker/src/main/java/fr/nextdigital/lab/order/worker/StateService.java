package fr.nextdigital.lab.order.worker;

import fr.nextdigital.lab.order.worker.domain.Order;
import fr.nextdigital.lab.order.worker.domain.OrderStatus;
import fr.nextdigital.lab.order.worker.event.OrderEvent;
import fr.nextdigital.lab.order.worker.event.OrderEventType;
import fr.nextdigital.lab.order.worker.event.OrderEvents;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * The {@link StateService} provides factory access to get new state machines for
 * replicating the state of an {@link Order} from {@link OrderEvents}.
 *
 * @author kbastani
 */
@Service
public class StateService {

    private final StateMachineFactory<OrderStatus, OrderEventType> factory;

    public StateService(StateMachineFactory<OrderStatus, OrderEventType> factory) {
        this.factory = factory;
    }

    /**
     * Create a new state machine that is initially configured and ready for replicating
     * the state of an {@link Order} from a sequence of {@link OrderEvent}.
     *
     * @return a new instance of {@link StateMachine}
     */
    public StateMachine<OrderStatus, OrderEventType> newStateMachine() {
        // Create a new state machine in its initial state
        StateMachine<OrderStatus, OrderEventType> stateMachine =
                factory.getStateMachine(UUID.randomUUID().toString());

        // Start the new state machine
        stateMachine.start();

        return stateMachine;
    }
}
