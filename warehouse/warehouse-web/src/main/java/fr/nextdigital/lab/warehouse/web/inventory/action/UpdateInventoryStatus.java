package fr.nextdigital.lab.warehouse.web.inventory.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.warehouse.web.inventory.domain.Inventory;
import fr.nextdigital.lab.warehouse.web.inventory.domain.InventoryService;
import fr.nextdigital.lab.warehouse.web.inventory.domain.InventoryStatus;
import fr.nextdigital.lab.warehouse.web.reservation.domain.ReservationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Updates the status of a {@link Inventory} entity.
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class UpdateInventoryStatus extends Action<Inventory> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ReservationService reservationService;
    private final InventoryService inventoryService;

    public UpdateInventoryStatus(ReservationService reservationService, InventoryService inventoryService) {
        this.reservationService = reservationService;
        this.inventoryService = inventoryService;
    }

    public Inventory apply(Inventory inventory, InventoryStatus inventoryStatus) {
        // Save rollback status
        InventoryStatus rollbackStatus = inventory.getStatus();

        try {
            // Update status
            inventory.setStatus(inventoryStatus);
            inventory = inventoryService.update(inventory);
        } catch (Exception ex) {
            log.error("Could not update the status", ex);
            inventory.setStatus(rollbackStatus);
            inventory = inventoryService.update(inventory);
        }

        return inventory;
    }
}
