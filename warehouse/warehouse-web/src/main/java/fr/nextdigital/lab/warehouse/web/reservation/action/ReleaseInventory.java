package fr.nextdigital.lab.warehouse.web.reservation.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.warehouse.web.inventory.domain.Inventory;
import fr.nextdigital.lab.warehouse.web.inventory.domain.InventoryService;
import fr.nextdigital.lab.warehouse.web.inventory.domain.InventoryStatus;
import fr.nextdigital.lab.warehouse.web.inventory.event.InventoryEvent;
import fr.nextdigital.lab.warehouse.web.reservation.domain.Reservation;
import fr.nextdigital.lab.warehouse.web.reservation.domain.ReservationModule;
import fr.nextdigital.lab.warehouse.web.reservation.domain.ReservationService;
import fr.nextdigital.lab.warehouse.web.reservation.domain.ReservationStatus;
import fr.nextdigital.lab.warehouse.web.reservation.event.ReservationEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static fr.nextdigital.lab.warehouse.web.inventory.event.InventoryEventType.INVENTORY_RELEASED;
import static fr.nextdigital.lab.warehouse.web.reservation.event.ReservationEventType.RESERVATION_FAILED;

/**
 * Release inventory for a {@link Reservation}.
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class ReleaseInventory extends Action<Reservation> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final InventoryService inventoryService;

    public ReleaseInventory(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public Reservation apply(Reservation reservation) {
        Assert.isTrue(reservation.getStatus() != ReservationStatus.RESERVATION_FAILED,
                "Reservation is already in a failed state");

        ReservationService reservationService = reservation.getModule(ReservationModule.class).getDefaultService();

        Inventory inventory = reservation.getInventory();

        try {
            // Remove the inventory and set the reservation to failed
            reservation.setInventory(null);
            reservation.setStatus(ReservationStatus.RESERVATION_FAILED);
            reservation = reservationService.update(reservation);

            // Trigger the reservation failed event
            reservation.sendAsyncEvent(new ReservationEvent(RESERVATION_FAILED, reservation));
        } catch (Exception ex) {
            log.error("Could not release the reservation's inventory", ex);
            if (reservation.getStatus() == ReservationStatus.RESERVATION_FAILED) {
                // Rollback the attempt
                reservation.setInventory(inventory);
                reservation.setStatus(ReservationStatus.RESERVATION_SUCCEEDED);
                reservation = reservationService.update(reservation);
            }
        } finally {
            if (inventory != null && reservation.getStatus() != ReservationStatus.RESERVATION_SUCCEEDED) {
                // Release the inventory
                inventory.setReservation(null);
                inventory.setStatus(InventoryStatus.RESERVATION_PENDING);
                inventory = inventoryService.update(inventory);

                // Trigger the inventory released event
                inventory.sendAsyncEvent(new InventoryEvent(INVENTORY_RELEASED, inventory));
            }
        }

        return reservation;
    }
}
