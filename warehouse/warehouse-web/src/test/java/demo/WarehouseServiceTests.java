package fr.nextdigital.lab.warehouse.worker;

import fr.nextdigital.lab.warehouse.web.inventory.domain.Inventory;
import fr.nextdigital.lab.warehouse.web.inventory.domain.InventoryService;
import fr.nextdigital.lab.warehouse.web.inventory.event.InventoryEventService;
import fr.nextdigital.lab.warehouse.web.inventory.repository.InventoryRepository;
import fr.nextdigital.lab.warehouse.web.reservation.domain.Reservation;
import fr.nextdigital.lab.warehouse.web.reservation.domain.ReservationService;
import fr.nextdigital.lab.warehouse.web.reservation.event.ReservationEventService;
import fr.nextdigital.lab.warehouse.web.reservation.repository.ReservationRepository;
import fr.nextdigital.lab.warehouse.web.warehouse.domain.Warehouse;
import fr.nextdigital.lab.warehouse.web.warehouse.event.WarehouseEventService;
import fr.nextdigital.lab.warehouse.web.warehouse.repository.WarehouseRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class WarehouseServiceTests {

    @MockBean
    private InventoryEventService inventoryEventService;

    @MockBean
    private WarehouseEventService warehouseEventService;

    @MockBean
    private ReservationEventService reservationEventService;

    @MockBean
    private RedissonClient redissonClient;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @MockBean
    private DiscoveryClient discoveryClient;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ReservationService reservationService;

    @Test
    public void saveReservationReturnsReservation() throws Exception {

        Warehouse warehouse = new Warehouse();
        warehouse = warehouseRepository.save(warehouse);

        Inventory inventory = new Inventory();
        inventory.setProductId("SKU-001");
        inventory.setWarehouse(warehouse);
        inventory = inventoryRepository.save(inventory);

        Inventory inventory1 = new Inventory();
        inventory1.setProductId("SKU-002");
        inventory.setWarehouse(warehouse);
        inventory1 = inventoryRepository.save(inventory1);

        Inventory inventory2 = new Inventory();
        inventory2.setProductId("SKU-003");
        inventory.setWarehouse(warehouse);
        inventory2 = inventoryRepository.save(inventory2);

        Reservation expected = new Reservation();
        expected.setInventory(inventory);
        expected = reservationRepository.save(expected);

        assertThat(expected.getIdentity()).isNotNull();

        Reservation actualReservation = reservationService.get(1L);
        Inventory actualInventory = inventoryService.get(1L);

        assertThat(actualReservation).isNotNull();
        assertThat(actualReservation.getInventory()).isNotNull();

        assertThat(actualInventory).isNotNull();

        actualInventory = inventoryService.get(1L);
        assertThat(actualInventory.getWarehouse()).isNotNull();

        List<Warehouse> warehouseFound = warehouseRepository.findAllWithInventory(3L, Arrays
                .asList("SKU-001", "SKU-002", "SKU-003"));

        List<Warehouse> warehouseNotFound = warehouseRepository.findAllWithInventory(2L, Arrays
                .asList("SKU-001", "SKU-099"));

    }

}