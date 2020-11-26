package fr.nextdigital.lab.payment.web.domain;

import fr.nextdigital.lab.domain.Module;
import fr.nextdigital.lab.event.EventService;
import fr.nextdigital.lab.payment.web.event.PaymentEvent;

@org.springframework.stereotype.Service
public class PaymentModule extends Module<Payment> {

    private final PaymentService paymentService;
    private final EventService<PaymentEvent, Long> eventService;

    public PaymentModule(PaymentService paymentService, EventService<PaymentEvent, Long> eventService) {
        this.paymentService = paymentService;
        this.eventService = eventService;
    }

    @Override
    public PaymentService getDefaultService() {
        return paymentService;
    }

    @Override
    public EventService<PaymentEvent, Long> getDefaultEventService() {
        return eventService;
    }
}
