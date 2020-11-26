package fr.nextdigital.lab.account.web.order.domain;

import fr.nextdigital.lab.domain.Module;
import fr.nextdigital.lab.account.web.event.AccountEvent;
import fr.nextdigital.lab.event.EventService;

@org.springframework.stereotype.Service
public class OrderModule extends Module<Order> {

    private final OrderService orderService;
    private final EventService<AccountEvent, Long> eventService;

    public OrderModule(OrderService orderService, EventService<AccountEvent, Long> eventService) {
        this.orderService = orderService;
        this.eventService = eventService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public EventService<AccountEvent, Long> getEventService() {
        return eventService;
    }

    @Override
    public OrderService getDefaultService() {
        return orderService;
    }

    @Override
    public EventService<AccountEvent, Long> getDefaultEventService() {
        return eventService;
    }
}
