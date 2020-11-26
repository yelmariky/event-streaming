package fr.nextdigital.lab.warehouse.worker.reservation.function;

import fr.nextdigital.lab.warehouse.worker.reservation.domain.Reservation;
import fr.nextdigital.lab.warehouse.worker.reservation.domain.ReservationStatus;
import fr.nextdigital.lab.warehouse.worker.reservation.event.ReservationEvent;
import fr.nextdigital.lab.warehouse.worker.reservation.event.ReservationEventType;
import org.slf4j.Logger;
import org.springframework.statemachine.StateContext;

import java.util.function.Function;

public class InventoryConnected extends ReservationFunction {

    final private Logger log = org.slf4j.LoggerFactory.getLogger(InventoryConnected.class);

    public InventoryConnected(StateContext<ReservationStatus, ReservationEventType> context, Function<ReservationEvent,
            Reservation> lambda) {
        super(context, lambda);
    }

    /**
     * Apply an {@link ReservationEvent} to the lambda function that was provided through the
     * constructor of this {@link ReservationFunction}.
     *
     * @param event is the {@link ReservationEvent} to apply to the lambda function
     */
    @Override
    public Reservation apply(ReservationEvent event) {
        log.info("Executing workflow for reservation inventory connected...");
        return super.apply(event);
    }
}
