package fr.nextdigital.lab.order.web.payment.domain;

import fr.nextdigital.lab.domain.Module;
import fr.nextdigital.lab.event.EventService;
import fr.nextdigital.lab.order.web.event.OrderEvent;

@org.springframework.stereotype.Service
public class PaymentModule extends Module<Payment> {

    private final PaymentService paymentService;
    private final EventService<OrderEvent, Long> eventService;

    public PaymentModule(PaymentService paymentService, EventService<OrderEvent, Long> eventService) {
        this.paymentService = paymentService;
        this.eventService = eventService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public EventService<OrderEvent, Long> getEventService() {
        return eventService;
    }

    @Override
    public PaymentService getDefaultService() {
        return paymentService;
    }

    @Override
    public EventService<OrderEvent, Long> getDefaultEventService() {
        return eventService;
    }
}
