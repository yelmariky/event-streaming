package fr.nextdigital.lab.warehouse.web.reservation.repository;

import fr.nextdigital.lab.warehouse.web.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findReservationsByOrderId(@Param("orderId") Long orderId);
}
