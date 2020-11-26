package fr.nextdigital.lab.warehouse.worker.inventory;

import fr.nextdigital.lab.warehouse.worker.inventory.domain.Inventory;
import fr.nextdigital.lab.warehouse.worker.inventory.domain.InventoryStatus;
import fr.nextdigital.lab.warehouse.worker.inventory.event.InventoryEvent;
import fr.nextdigital.lab.warehouse.worker.inventory.event.InventoryEventType;
import fr.nextdigital.lab.warehouse.worker.inventory.event.InventoryEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryStateFactory {

    final private Logger log = LoggerFactory.getLogger(InventoryStateFactory.class);
    final private InventoryStateService stateService;

    public InventoryStateFactory(InventoryStateService stateService) {
        this.stateService = stateService;
    }

    public Inventory apply(InventoryEvent inventoryEvent) {
        Assert.notNull(inventoryEvent, "Cannot apply a null event");
        Assert.notNull(inventoryEvent.getId(), "The event payload's identity link was not found");

        StateMachine<InventoryStatus, InventoryEventType> stateMachine = getStateMachine(inventoryEvent);
        stateMachine.stop();

        return stateMachine.getExtendedState().get("inventory", Inventory.class);
    }

    private StateMachine<InventoryStatus, InventoryEventType> getStateMachine(InventoryEvent inventoryEvent) {
        Link eventId = inventoryEvent.getId();
        log.info(String.format("Inventory event received: %s", eventId));

        StateMachine<InventoryStatus, InventoryEventType> stateMachine;
        Map<String, Object> contextMap;
        InventoryEvents eventLog;

        eventLog = getEventLog(inventoryEvent);
        contextMap = getEventHeaders(inventoryEvent);
        stateMachine = stateService.newStateMachine();

        // Replicate the aggregate state
        eventLog.getContent().stream()
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .forEach(e -> stateMachine.sendEvent(MessageBuilder.createMessage(e.getType(), e.getId()
                        .equals(eventId) ? new MessageHeaders(contextMap) : new MessageHeaders(null))));

        return stateMachine;
    }

    private Map<String, Object> getEventHeaders(InventoryEvent inventoryEvent) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("event", inventoryEvent);
        return headerMap;
    }

    private InventoryEvents getEventLog(InventoryEvent event) {
        // Follow the hypermedia link to fetch the attached inventory
        Traverson traverson = new Traverson(
                URI.create(event.getLink("inventory")
                        .getHref()),
                MediaTypes.HAL_JSON
        );

        // Get the event log for the attached inventory resource
        return traverson.follow("events")
                .toEntity(InventoryEvents.class)
                .getBody();
    }
}
