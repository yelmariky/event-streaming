package fr.nextdigital.lab.warehouse.web.warehouse.repository;

import fr.nextdigital.lab.warehouse.web.inventory.domain.Inventory;
import fr.nextdigital.lab.warehouse.web.warehouse.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    @Query("select w from Warehouse w where :productCount = (select count(distinct item.productId) from Warehouse w2 " +
            "inner join w2.inventory item\n" +
            "where item.productId in :productIds\n" +
            "and w = w2)")
    List<Warehouse> findAllWithInventory(@Param("productCount") Long productCount, @Param("productIds")
            Collection<String> productIds);
}
