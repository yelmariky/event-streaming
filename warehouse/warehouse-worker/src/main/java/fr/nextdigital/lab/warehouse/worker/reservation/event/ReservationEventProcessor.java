package fr.nextdigital.lab.warehouse.worker.reservation.event;

import fr.nextdigital.lab.warehouse.worker.reservation.ReservationStateFactory;
import fr.nextdigital.lab.warehouse.worker.reservation.domain.Reservation;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;

/**
 * The {@link ReservationEventProcessor} monitors for a variety of {@link ReservationEvent} domain
 * events for an {@link Reservation}.
 *
 * @author kbastani
 */
@EnableAutoConfiguration
@EnableBinding(ReservationEventSink.class)
@Profile({"cloud", "development", "docker"})
public class ReservationEventProcessor {

    private ReservationStateFactory stateFactory;

    public ReservationEventProcessor(ReservationStateFactory stateFactory) {
        this.stateFactory = stateFactory;
    }

    @StreamListener(ReservationEventSink.INPUT)
    public void streamListener(ReservationEvent reservationEvent) {
        stateFactory.apply(reservationEvent);
    }
}
