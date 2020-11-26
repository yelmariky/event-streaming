package fr.nextdigital.lab.warehouse.worker.inventory.function;

import fr.nextdigital.lab.warehouse.worker.inventory.domain.Inventory;
import fr.nextdigital.lab.warehouse.worker.inventory.domain.InventoryStatus;
import fr.nextdigital.lab.warehouse.worker.inventory.event.InventoryEvent;
import fr.nextdigital.lab.warehouse.worker.inventory.event.InventoryEventType;
import org.slf4j.Logger;
import org.springframework.statemachine.StateContext;

import java.util.function.Function;

public class InventoryReleased extends InventoryFunction {

    final private Logger log = org.slf4j.LoggerFactory.getLogger(InventoryReleased.class);

    public InventoryReleased(StateContext<InventoryStatus, InventoryEventType> context, Function<InventoryEvent,
            Inventory> lambda) {
        super(context, lambda);
    }

    /**
     * Apply an {@link InventoryEvent} to the lambda function that was provided through the
     * constructor of this {@link InventoryFunction}.
     *
     * @param event is the {@link InventoryEvent} to apply to the lambda function
     */
    @Override
    public Inventory apply(InventoryEvent event) {
        log.info("Executing workflow for inventory released...");
        return super.apply(event);
    }
}
