package fr.nextdigital.lab.warehouse.web.reservation.domain;

import fr.nextdigital.lab.domain.Service;
import fr.nextdigital.lab.warehouse.web.reservation.event.ReservationEvent;
import fr.nextdigital.lab.warehouse.web.reservation.event.ReservationEventService;
import fr.nextdigital.lab.warehouse.web.reservation.event.ReservationEventType;
import fr.nextdigital.lab.warehouse.web.reservation.repository.ReservationRepository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ReservationService extends Service<Reservation, Long> {

    private final ReservationRepository reservationRepository;
    private final ReservationEventService reservationEventService;

    public ReservationService(ReservationRepository reservationRepository, ReservationEventService
            reservationEventService) {
        this.reservationRepository = reservationRepository;
        this.reservationEventService = reservationEventService;
    }

    /**
     * Create a new {@link Reservation} entity.
     *
     * @param reservation is the {@link Reservation} to create
     * @return the newly created {@link Reservation}
     */
    public Reservation create(Reservation reservation) {

        // Save the reservation to the repository
        reservation = reservationRepository.saveAndFlush(reservation);

        return reservation;
    }

    /**
     * Get an {@link Reservation} entity for the supplied identifier.
     *
     * @param id is the unique identifier of a {@link Reservation} entity
     * @return an {@link Reservation} entity
     */
    public Reservation get(Long id) {
        return reservationRepository.findById(id).get();
    }

    /**
     * Update an {@link Reservation} entity with the supplied identifier.
     *
     * @param reservation is the {@link Reservation} containing updated fields
     * @return the updated {@link Reservation} entity
     */
    public Reservation update(Reservation reservation) {
        Assert.notNull(reservation.getIdentity(), "Reservation id must be present in the resource URL");
        Assert.notNull(reservation, "Reservation request body cannot be null");

        Assert.state(reservationRepository.existsById(reservation.getIdentity()),
                "The reservation with the supplied id does not exist");

        Reservation currentReservation = get(reservation.getIdentity());
        currentReservation.setStatus(reservation.getStatus());
        currentReservation.setOrderId(reservation.getOrderId());
        currentReservation.setProductId(reservation.getProductId());
        currentReservation.setWarehouse(reservation.getWarehouse());
        currentReservation.setInventory(reservation.getInventory());

        return reservationRepository.saveAndFlush(currentReservation);
    }

    /**
     * Delete the {@link Reservation} with the supplied identifier.
     *
     * @param id is the unique identifier for the {@link Reservation}
     */
    public boolean delete(Long id) {
        Assert.state(reservationRepository.existsById(id),
                "The reservation with the supplied id does not exist");
        this.reservationRepository.deleteById(id);
        return true;
    }

    public List<Reservation> create(List<Reservation> reservations) {
        Assert.notEmpty(reservations, "Reservation list must not be empty");
        List<Reservation> reservationList = this.reservationRepository.saveAll(reservations);

        // Trigger reservation created events
        reservationList
                .forEach(r -> r.sendAsyncEvent(new ReservationEvent(ReservationEventType.RESERVATION_CREATED, r)));

        return reservationList;
    }

    public Reservations findReservationsByOrderId(Long orderId) {
        return new Reservations(reservationRepository.findReservationsByOrderId(orderId));
    }
}
