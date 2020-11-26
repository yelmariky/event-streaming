package fr.nextdigital.lab.payment.web.event;

import fr.nextdigital.lab.event.EventRepository;

/**
 * The repository for managing the persistence of {@link PaymentEvent}s.
 *
 * @author Kenny Bastani
 */
public interface PaymentEventRepository extends EventRepository<PaymentEvent, Long> {
}
