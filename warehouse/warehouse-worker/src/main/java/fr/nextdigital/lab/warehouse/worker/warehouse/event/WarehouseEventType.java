package fr.nextdigital.lab.warehouse.worker.warehouse.event;

import fr.nextdigital.lab.warehouse.worker.warehouse.domain.Warehouse;
import fr.nextdigital.lab.warehouse.worker.warehouse.domain.WarehouseStatus;

/**
 * The {@link WarehouseEventType} represents a collection of possible events that describe state transitions of
 * {@link WarehouseStatus} on the {@link Warehouse} aggregate.
 *
 * @author Kenny Bastani
 */
public enum WarehouseEventType {
    WAREHOUSE_CREATED
}
