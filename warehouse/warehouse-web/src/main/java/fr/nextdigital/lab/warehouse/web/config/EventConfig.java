package fr.nextdigital.lab.warehouse.web.config;

import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import fr.nextdigital.lab.event.EventSource;
import fr.nextdigital.lab.warehouse.web.inventory.config.InventoryEventSource;
import fr.nextdigital.lab.warehouse.web.inventory.event.InventoryEventRepository;
import fr.nextdigital.lab.warehouse.web.inventory.event.InventoryEventService;
import fr.nextdigital.lab.warehouse.web.reservation.config.ReservationEventSource;
import fr.nextdigital.lab.warehouse.web.reservation.event.ReservationEventRepository;
import fr.nextdigital.lab.warehouse.web.reservation.event.ReservationEventService;
import fr.nextdigital.lab.warehouse.web.warehouse.config.WarehouseEventSource;
import fr.nextdigital.lab.warehouse.web.warehouse.event.WarehouseEventRepository;
import fr.nextdigital.lab.warehouse.web.warehouse.event.WarehouseEventService;

/**
 * Overrides default auto-configuration for the default event service.
 *
 * @author Kenny Bastani
 */
@Configuration
public class EventConfig {

    @Bean
    public EventSource inventoryChannel(InventoryEventSource eventSource) {
        return new EventSource(eventSource.output());
    }

    @Bean
    public EventSource warehouseChannel(WarehouseEventSource eventSource) {
        return new EventSource(eventSource.output());
    }

    @Bean
    public EventSource reservationChannel(ReservationEventSource eventSource) {
        return new EventSource(eventSource.output());
    }

    @Bean
    public InventoryEventService inventoryEventService(RestTemplate restTemplate, InventoryEventRepository
            inventoryEventRepository, InventoryEventSource eventStream, Source source) {
        return new InventoryEventService(inventoryEventRepository, inventoryChannel(eventStream), restTemplate, source);
    }

    @Bean
    public WarehouseEventService warehouseEventService(RestTemplate restTemplate, WarehouseEventRepository
            warehouseEventRepository, WarehouseEventSource eventStream, Source source) {
        return new WarehouseEventService(warehouseEventRepository, warehouseChannel(eventStream), restTemplate, source);
    }

    @Bean
    public ReservationEventService reservationEventService(RestTemplate restTemplate, ReservationEventRepository
            reservationEventRepository, ReservationEventSource eventStream, Source source) {
        return new ReservationEventService(reservationEventRepository, reservationChannel(eventStream), restTemplate,
                source);
    }


}
