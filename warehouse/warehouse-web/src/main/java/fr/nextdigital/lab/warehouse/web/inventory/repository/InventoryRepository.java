package fr.nextdigital.lab.warehouse.web.inventory.repository;

import fr.nextdigital.lab.warehouse.web.inventory.domain.Inventory;
import fr.nextdigital.lab.warehouse.web.inventory.domain.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findFirstInventoryByWarehouseIdAndProductIdAndStatus(@Param("warehouseId") Long warehouseId,
            @Param("productId") String productId, @Param("status") InventoryStatus status);
}
