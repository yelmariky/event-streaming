package fr.nextdigital.lab.payment.worker.state;

import fr.nextdigital.lab.payment.worker.event.PaymentEventType;
import fr.nextdigital.lab.payment.worker.payment.PaymentStatus;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * The {@link StateMachineService} provides factory access to get new state machines for
 * replicating the state of an {@link fr.nextdigital.lab.payment.worker.payment.Payment} from {@link fr.nextdigital.lab.payment.worker.event.PaymentEvents}.
 *
 * @author kbastani
 */
@Service
public class StateMachineService {

    private final StateMachineFactory<PaymentStatus, PaymentEventType> factory;

    public StateMachineService(StateMachineFactory<PaymentStatus, PaymentEventType> factory) {
        this.factory = factory;
    }

    /**
     * Create a new state machine that is initially configured and ready for replicating
     * the state of an {@link fr.nextdigital.lab.payment.worker.payment.Payment} from a sequence of {@link fr.nextdigital.lab.payment.worker.event.PaymentEvent}.
     *
     * @return a new instance of {@link StateMachine}
     */
    public StateMachine<PaymentStatus, PaymentEventType> getStateMachine() {
        // Create a new state machine in its initial state
        StateMachine<PaymentStatus, PaymentEventType> stateMachine =
                factory.getStateMachine(UUID.randomUUID().toString());

        // Start the new state machine
        stateMachine.start();

        return stateMachine;
    }
}
