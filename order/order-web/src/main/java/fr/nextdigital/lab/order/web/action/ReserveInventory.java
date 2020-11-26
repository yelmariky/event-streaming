package fr.nextdigital.lab.order.web.action;

import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.order.web.domain.Order;
import fr.nextdigital.lab.order.web.domain.OrderModule;
import fr.nextdigital.lab.order.web.domain.OrderService;
import fr.nextdigital.lab.order.web.domain.OrderStatus;
import fr.nextdigital.lab.order.web.event.OrderEvent;
import fr.nextdigital.lab.order.web.event.OrderEventType;
import fr.nextdigital.lab.order.web.warehouse.domain.Warehouse;
import fr.nextdigital.lab.order.web.warehouse.domain.WarehouseService;
import fr.nextdigital.lab.order.web.warehouse.exception.WarehouseNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Reserves inventory for an {@link Order}.
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class ReserveInventory extends Action<Order> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(ReserveInventory.class);
    private final WarehouseService warehouseService;

    public ReserveInventory(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    public Order apply(Order order) {
        Assert.isTrue(!Arrays
                .asList(OrderStatus.PAYMENT_SUCCEEDED, OrderStatus.PAYMENT_PENDING,
                        OrderStatus.PAYMENT_FAILED, OrderStatus.INVENTORY_RESERVED,
                        OrderStatus.RESERVATION_SUCCEEDED, OrderStatus.RESERVATION_PENDING,
                        OrderStatus.RESERVATION_FAILED)
                .contains(order.getStatus()), "Inventory has already been reserved");
        Assert.isTrue(order
                .getStatus() == OrderStatus.ACCOUNT_CONNECTED, "The order must be connected to an account");

        Warehouse warehouse;

        OrderService orderService = order.getModule(OrderModule.class).getDefaultService();

        OrderStatus status = order.getStatus();
        order.setStatus(OrderStatus.RESERVATION_PENDING);
        order = orderService.update(order);

        try {
            warehouse = warehouseService.findWarehouseWithInventory(order);
        } catch (WarehouseNotFoundException ex) {
            log.error("The order contains items that are not available at any warehouse", ex);
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error connecting to warehouse service", ex);
            // Rollback status change
            order.setStatus(status);
            order = orderService.update(order);
            throw ex;
        }

        try {
            // Reserve inventory for the order from the returned warehouse
            warehouse = warehouseService.reserveInventory(warehouse, order);
        } catch (Exception ex) {
            log.error("Could not reserve inventory for the order", ex);

            order.setStatus(OrderStatus.ACCOUNT_CONNECTED);
            order = orderService.update(order);

            OrderEvent event = new OrderEvent(OrderEventType.RESERVATION_FAILED, order);
            event.add(warehouse.getLink("self").withRel("warehouse"));

            // Trigger reservation failed
            order.sendAsyncEvent(event);
        } finally {
            if(order.getStatus() != OrderStatus.ACCOUNT_CONNECTED) {
                OrderEvent event = new OrderEvent(OrderEventType.RESERVATION_PENDING, order);
                event.add(warehouse.getLink("self").withRel("warehouse"));

                // Trigger reservation pending event
                order.sendAsyncEvent(event);
            }
        }

        return order;
    }
}
