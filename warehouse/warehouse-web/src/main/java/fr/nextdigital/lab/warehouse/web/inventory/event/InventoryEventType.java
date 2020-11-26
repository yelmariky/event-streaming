package fr.nextdigital.lab.warehouse.web.inventory.event;

import fr.nextdigital.lab.warehouse.web.inventory.domain.Inventory;
import fr.nextdigital.lab.warehouse.web.inventory.domain.InventoryStatus;

/**
 * The {@link InventoryEventType} represents a collection of possible events that describe state transitions of
 * {@link InventoryStatus} on the {@link Inventory} aggregate.
 *
 * @author Kenny Bastani
 */
public enum InventoryEventType {
    INVENTORY_CREATED,
    RESERVATION_PENDING,
    RESERVATION_CONNECTED,
    INVENTORY_RESERVED,
    INVENTORY_RELEASED
}
