package fr.nextdigital.lab.payment.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import fr.nextdigital.lab.payment.web.domain.Payment;

/**
 * The repository for managing the persistence of {@link Payment} entities.
 *
 * @author Kenny Bastani
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findPaymentByOrderId(@Param("orderId") Long orderId);
}
