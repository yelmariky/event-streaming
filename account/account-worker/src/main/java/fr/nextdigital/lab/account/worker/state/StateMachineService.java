package fr.nextdigital.lab.account.worker.state;

import fr.nextdigital.lab.account.worker.domain.AccountStatus;
import fr.nextdigital.lab.account.worker.event.AccountEventType;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * The {@link StateMachineService} provides factory access to get new state machines for
 * replicating the state of an {@link fr.nextdigital.lab.account.worker.domain.Account} from {@link fr.nextdigital.lab.account.worker.event.AccountEvents}.
 *
 * @author kbastani
 */
@Service
public class StateMachineService {

    private final StateMachineFactory<AccountStatus, AccountEventType> factory;

    public StateMachineService(StateMachineFactory<AccountStatus, AccountEventType> factory) {
        this.factory = factory;
    }

    /**
     * Create a new state machine that is initially configured and ready for replicating
     * the state of an {@link fr.nextdigital.lab.account.worker.domain.Account} from a sequence of {@link fr.nextdigital.lab.account.worker.event.AccountEvent}.
     *
     * @return a new instance of {@link StateMachine}
     */
    public StateMachine<AccountStatus, AccountEventType> getStateMachine() {
        // Create a new state machine in its initial state
        StateMachine<AccountStatus, AccountEventType> stateMachine =
                factory.getStateMachine(UUID.randomUUID().toString());

        // Start the new state machine
        stateMachine.start();

        return stateMachine;
    }
}
