package fr.nextdigital.lab.warehouse.web.inventory.domain;


import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import fr.nextdigital.lab.domain.Service;
import fr.nextdigital.lab.warehouse.web.inventory.repository.InventoryRepository;
import fr.nextdigital.lab.warehouse.web.reservation.domain.Reservation;

import java.util.concurrent.TimeUnit;

@org.springframework.stereotype.Service
public class InventoryService extends Service<Inventory, Long> {

    private final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;
  //  private final RedissonClient redissonClient;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
      //  this.redissonClient = redissonClient;
    }

    /**
     * Create a new {@link Inventory} entity.
     *
     * @param inventory is the {@link Inventory} to create
     * @return the newly created {@link Inventory}
     */
    public Inventory create(Inventory inventory) {

        // Save the inventory to the repository
        inventory = inventoryRepository.saveAndFlush(inventory);

        return inventory;
    }

    /**
     * Get an {@link Inventory} entity for the supplied identifier.
     *
     * @param id is the unique identifier of a {@link Inventory} entity
     * @return an {@link Inventory} entity
     */
    public Inventory get(Long id) {
        return inventoryRepository.findById(id).get();
    }

    /**
     * Update an {@link Inventory} entity with the supplied identifier.
     *
     * @param inventory is the {@link Inventory} containing updated fields
     * @return the updated {@link Inventory} entity
     */
    public Inventory update(Inventory inventory) {
        Assert.notNull(inventory.getIdentity(), "Inventory id must be present in the resource URL");
        Assert.notNull(inventory, "Inventory request body cannot be null");

        Assert.state(inventoryRepository.existsById(inventory.getIdentity()),
                "The inventory with the supplied id does not exist");

        Inventory currentInventory = get(inventory.getIdentity());
        currentInventory.setStatus(inventory.getStatus());
        currentInventory.setProductId(inventory.getProductId());
        currentInventory.setReservation(inventory.getReservation());
        currentInventory.setWarehouse(inventory.getWarehouse());

        return inventoryRepository.saveAndFlush(currentInventory);
    }

    /**
     * Delete the {@link Inventory} with the supplied identifier.
     *
     * @param id is the unique identifier for the {@link Inventory}
     */
    public boolean delete(Long id) {
        Assert.state(inventoryRepository.existsById(id),
                "The inventory with the supplied id does not exist");
        this.inventoryRepository.deleteById(id);
        return true;
    }

    /**
     * Find available inventory in the warehouse for a product identifier
     *
     * @param reservation is the reservation to connect to the inventory
     * @return the first available inventory in the warehouse or null
     */
    public Inventory findAvailableInventory(Reservation reservation) {
        Assert.notNull(reservation.getWarehouse(), "Reservation must be connected to a warehouse");
        Assert.notNull(reservation.getProductId(), "Reservation must contain a valid product identifier");

        Boolean reserved = false;
        Inventory inventory = null;

        while (!reserved) {
            inventory = inventoryRepository
                    .findFirstInventoryByWarehouseIdAndProductIdAndStatus(reservation.getWarehouse()
                            .getIdentity(), reservation.getProductId(), InventoryStatus.RESERVATION_PENDING);
            if (inventory != null) {
                // Acquire lock
               /* RLock inventoryLock = redissonClient
                        .getLock(String.format("inventory_%s", inventory.getIdentity().toString()));
*/
                Boolean lock = false;

              //  try {
                   // lock = inventoryLock.tryLock(1, 5000, TimeUnit.MILLISECONDS);
                	lock = true;
            /*    } catch (InterruptedException e) {
                    log.error("Interrupted while acquiring lock on inventory", e);
                }
*/
                if (lock) {
                    inventory.setStatus(InventoryStatus.RESERVATION_CONNECTED);
                    inventory = update(inventory);

                    // Reserve the inventory
                    inventory = inventory.reserve(reservation.getIdentity());

                //    inventoryLock.unlock();
                }

                reserved = lock;
            } else {
                reserved = true;
            }
        }

        return inventory;
    }
}
