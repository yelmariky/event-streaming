package fr.nextdigital.lab.warehouse.web.inventory.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.warehouse.web.inventory.domain.Inventory;
import fr.nextdigital.lab.warehouse.web.inventory.domain.InventoryService;
import fr.nextdigital.lab.warehouse.web.inventory.domain.InventoryStatus;
import fr.nextdigital.lab.warehouse.web.inventory.event.InventoryEvent;
import fr.nextdigital.lab.warehouse.web.inventory.event.InventoryEventType;
import fr.nextdigital.lab.warehouse.web.reservation.domain.Reservation;
import fr.nextdigital.lab.warehouse.web.reservation.domain.ReservationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static fr.nextdigital.lab.warehouse.web.inventory.domain.InventoryStatus.RESERVATION_CONNECTED;

/**
 * Reserves inventory for an {@link Inventory}.
 *
 * @author Kenny Bastani
 */
@Service
public class ReserveInventory extends Action<Inventory> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ReservationService reservationService;
    private final InventoryService inventoryService;

    public ReserveInventory(ReservationService reservationService, InventoryService inventoryService) {
        this.reservationService = reservationService;
        this.inventoryService = inventoryService;
    }

    public Inventory apply(Inventory inventory, Long reservationId) {
        Assert.isTrue(inventory.getStatus() == InventoryStatus.RESERVATION_CONNECTED,
                "Inventory must be in a reservation connected state");
        Assert.isTrue(inventory.getReservation() == null,
                "There is already a reservation attached to the inventory");

        Reservation reservation = reservationService.get(reservationId);
        Assert.notNull(reservation, "Reserve inventory failed, the reservation does not exist");

        try {
            // Trigger the reservation connected event
            inventory.sendAsyncEvent(new InventoryEvent(InventoryEventType.RESERVATION_CONNECTED, inventory));
        } catch (Exception ex) {
            log.error("Could not connect reservation to inventory", ex);
            inventory.setReservation(null);
            inventory.setStatus(InventoryStatus.RESERVATION_PENDING);
            inventory = inventoryService.update(inventory);
        } finally {
            if (inventory.getStatus() == RESERVATION_CONNECTED && inventory.getReservation() != null) {
                inventory.setStatus(InventoryStatus.INVENTORY_RESERVED);
                inventory = inventoryService.update(inventory);
                inventory.sendAsyncEvent(new InventoryEvent(InventoryEventType.INVENTORY_RESERVED, inventory));
            }
        }

        return inventory;
    }
}
