package fr.nextdigital.lab.warehouse.worker.warehouse;

import fr.nextdigital.lab.warehouse.worker.warehouse.domain.Warehouse;
import fr.nextdigital.lab.warehouse.worker.warehouse.domain.WarehouseStatus;
import fr.nextdigital.lab.warehouse.worker.warehouse.event.WarehouseEvent;
import fr.nextdigital.lab.warehouse.worker.warehouse.event.WarehouseEventType;
import fr.nextdigital.lab.warehouse.worker.warehouse.event.WarehouseEvents;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * The {@link WarehouseStateService} provides factory access to get new state machines for
 * replicating the state of an {@link Warehouse} from {@link WarehouseEvents}.
 *
 * @author kbastani
 */
@Service
public class WarehouseStateService {

    private final StateMachineFactory<WarehouseStatus, WarehouseEventType> factory;

    public WarehouseStateService(StateMachineFactory<WarehouseStatus, WarehouseEventType> warehouseStateMachineFactory) {
        this.factory = warehouseStateMachineFactory;
    }

    /**
     * Create a new state machine that is initially configured and ready for replicating
     * the state of an {@link Warehouse} from a sequence of {@link WarehouseEvent}.
     *
     * @return a new instance of {@link StateMachine}
     */
    public StateMachine<WarehouseStatus, WarehouseEventType> newStateMachine() {
        // Create a new state machine in its initial state
        StateMachine<WarehouseStatus, WarehouseEventType> stateMachine =
                factory.getStateMachine(UUID.randomUUID().toString());

        // Start the new state machine
        stateMachine.start();

        return stateMachine;
    }
}
