package fr.nextdigital.lab.warehouse.web.warehouse.event;

import fr.nextdigital.lab.warehouse.web.warehouse.domain.Warehouse;
import fr.nextdigital.lab.warehouse.web.warehouse.domain.WarehouseStatus;

/**
 * The {@link WarehouseEventType} represents a collection of possible events that describe state transitions of
 * {@link WarehouseStatus} on the {@link Warehouse} aggregate.
 *
 * @author Kenny Bastani
 */
public enum WarehouseEventType {
    WAREHOUSE_CREATED
}
