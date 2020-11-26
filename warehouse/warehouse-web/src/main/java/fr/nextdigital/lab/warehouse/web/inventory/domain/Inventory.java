package fr.nextdigital.lab.warehouse.web.inventory.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.nextdigital.lab.domain.Aggregate;
import fr.nextdigital.lab.domain.Command;
import fr.nextdigital.lab.domain.Module;
import fr.nextdigital.lab.warehouse.web.domain.AbstractEntity;
import fr.nextdigital.lab.warehouse.web.inventory.action.ReserveInventory;
import fr.nextdigital.lab.warehouse.web.inventory.action.UpdateInventoryStatus;
import fr.nextdigital.lab.warehouse.web.inventory.action.ReserveInventory;
import fr.nextdigital.lab.warehouse.web.inventory.controller.InventoryController;
import fr.nextdigital.lab.warehouse.web.inventory.event.InventoryEvent;
import fr.nextdigital.lab.warehouse.web.reservation.domain.Reservation;
import fr.nextdigital.lab.warehouse.web.warehouse.domain.Warehouse;

import org.springframework.hateoas.Link;

import javax.persistence.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Entity
public class Inventory extends AbstractEntity<InventoryEvent, Long> {
    @Id
    @GeneratedValue
    private Long id;

    private String productId;

    @Enumerated(value = EnumType.STRING)
    private InventoryStatus status;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Reservation reservation;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Warehouse warehouse;

    public Inventory() {
        this.status = InventoryStatus.INVENTORY_CREATED;
    }

    @JsonProperty("inventoryId")
    @Override
    public Long getIdentity() {
        return this.id;
    }

    @Override
    public void setIdentity(Long id) {
        this.id = id;
    }

    public InventoryStatus getStatus() {
        return status;
    }

    public void setStatus(InventoryStatus status) {
        this.status = status;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Command(method = "reserve", controller = InventoryController.class)
    public Inventory reserve(Long reservationId) {
        return getAction(ReserveInventory.class)
                .apply(this, reservationId);
    }

    @Command(method = "updateInventoryStatus", controller = InventoryController.class)
    public Inventory updateStatus(InventoryStatus status) {
        return getAction(UpdateInventoryStatus.class)
                .apply(this, status);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Module<A>, A extends Aggregate<InventoryEvent, Long>> T getModule() throws
            IllegalArgumentException {
        InventoryModule inventoryModule = getModule(InventoryModule.class);
        return (T) inventoryModule;
    }

    /**
     * Returns the {@link Link} with a rel of {@link Link#REL_SELF}.
     */
    @Override
    public Link getId() {
        Link link;
        try {
            link = linkTo(InventoryController.class)
                    .slash("inventory")
                    .slash(getIdentity())
                    .withSelfRel();
        } catch (Exception ex) {
            link = new Link("http://warehouse-service/v1/inventory/" + id, "self");
        }

        return link;
    }
}
