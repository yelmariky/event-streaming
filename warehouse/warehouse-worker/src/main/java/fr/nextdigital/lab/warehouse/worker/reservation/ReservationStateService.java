package fr.nextdigital.lab.warehouse.worker.reservation;

import fr.nextdigital.lab.warehouse.worker.reservation.domain.Reservation;
import fr.nextdigital.lab.warehouse.worker.reservation.domain.ReservationStatus;
import fr.nextdigital.lab.warehouse.worker.reservation.event.ReservationEvent;
import fr.nextdigital.lab.warehouse.worker.reservation.event.ReservationEventType;
import fr.nextdigital.lab.warehouse.worker.reservation.event.ReservationEvents;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * The {@link ReservationStateService} provides factory access to get new state machines for
 * replicating the state of an {@link Reservation} from {@link ReservationEvents}.
 *
 * @author kbastani
 */
@Service
public class ReservationStateService {

    private final StateMachineFactory<ReservationStatus, ReservationEventType> factory;

    public ReservationStateService(StateMachineFactory<ReservationStatus, ReservationEventType> reservationStateMachineFactory) {
        this.factory = reservationStateMachineFactory;
    }

    /**
     * Create a new state machine that is initially configured and ready for replicating
     * the state of an {@link Reservation} from a sequence of {@link ReservationEvent}.
     *
     * @return a new instance of {@link StateMachine}
     */
    public StateMachine<ReservationStatus, ReservationEventType> newStateMachine() {
        // Create a new state machine in its initial state
        StateMachine<ReservationStatus, ReservationEventType> stateMachine =
                factory.getStateMachine(UUID.randomUUID().toString());

        // Start the new state machine
        stateMachine.start();

        return stateMachine;
    }
}
