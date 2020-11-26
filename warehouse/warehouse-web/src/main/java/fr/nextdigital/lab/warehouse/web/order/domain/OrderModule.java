package fr.nextdigital.lab.warehouse.web.order.domain;

import fr.nextdigital.lab.warehouse.web.warehouse.event.WarehouseEvent;
import fr.nextdigital.lab.domain.Module;
import fr.nextdigital.lab.event.EventService;

@org.springframework.stereotype.Service
public class OrderModule extends Module<Order> {

    private final OrderService orderService;
    private final EventService<WarehouseEvent, Long> eventService;

    public OrderModule(OrderService orderService, EventService<WarehouseEvent, Long> eventService) {
        this.orderService = orderService;
        this.eventService = eventService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public EventService<WarehouseEvent, Long> getEventService() {
        return eventService;
    }

    @Override
    public OrderService getDefaultService() {
        return orderService;
    }

    @Override
    public EventService<WarehouseEvent, Long> getDefaultEventService() {
        return eventService;
    }
}
