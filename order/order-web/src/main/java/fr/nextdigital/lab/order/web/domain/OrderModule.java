package fr.nextdigital.lab.order.web.domain;

import fr.nextdigital.lab.domain.Module;
import fr.nextdigital.lab.event.EventService;
import fr.nextdigital.lab.order.web.event.OrderEvent;
import fr.nextdigital.lab.order.web.payment.domain.PaymentService;

@org.springframework.stereotype.Service
public class OrderModule extends Module<Order> {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final EventService<OrderEvent, Long> eventService;

    public OrderModule(OrderService orderService, PaymentService paymentService, EventService<OrderEvent, Long>
            eventService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.eventService = eventService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public EventService<OrderEvent, Long> getEventService() {
        return eventService;
    }

    @Override
    public OrderService getDefaultService() {
        return orderService;
    }

    @Override
    public EventService<OrderEvent, Long> getDefaultEventService() {
        return eventService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }
}
