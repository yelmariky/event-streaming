package fr.nextdigital.lab.warehouse.web.reservation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.nextdigital.lab.warehouse.web.domain.AbstractEntity;
import fr.nextdigital.lab.domain.Aggregate;
import fr.nextdigital.lab.domain.Command;
import fr.nextdigital.lab.domain.Module;
import fr.nextdigital.lab.warehouse.web.inventory.domain.Inventory;
import fr.nextdigital.lab.warehouse.web.reservation.action.ConnectOrder;
import fr.nextdigital.lab.warehouse.web.reservation.action.ConnectInventory;
import fr.nextdigital.lab.warehouse.web.reservation.action.ReleaseInventory;
import fr.nextdigital.lab.warehouse.web.reservation.controller.ReservationController;
import fr.nextdigital.lab.warehouse.web.reservation.event.ReservationEvent;
import fr.nextdigital.lab.warehouse.web.warehouse.domain.Warehouse;
import org.springframework.hateoas.Link;

import javax.persistence.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Entity(name="reservations")
public class Reservation extends AbstractEntity<ReservationEvent, Long> {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private ReservationStatus status;

    private String productId;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Inventory inventory;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Warehouse warehouse;

    private Long orderId;

    public Reservation() {
        this.status = ReservationStatus.RESERVATION_CREATED;
    }

    public Reservation(String productId, Long orderId) {
        this();
        this.productId = productId;
        this.orderId = orderId;
    }

    public Reservation(String productId, Long orderId, Warehouse warehouse) {
        this();
        this.productId = productId;
        this.warehouse = warehouse;
        this.orderId = orderId;
    }

    @JsonProperty("reservationId")
    @Override
    public Long getIdentity() {
        return this.id;
    }

    @Override
    public void setIdentity(Long id) {
        this.id = id;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Command(method = "connectInventory", controller = ReservationController.class)
    public Reservation connectInventory() {
        return getAction(ConnectInventory.class)
                .apply(this);
    }

    @Command(method = "releaseInventory", controller = ReservationController.class)
    public Reservation releaseInventory() {
        return getAction(ReleaseInventory.class)
                .apply(this);
    }

    @Command(method = "connectOrder", controller = ReservationController.class)
    public Reservation connectOrder(Long orderId) {
        return getAction(ConnectOrder.class)
                .apply(this, orderId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Module<A>, A extends Aggregate<ReservationEvent, Long>> T getModule() throws
            IllegalArgumentException {
        ReservationModule reservationModule = getModule(ReservationModule.class);
        return (T) reservationModule;
    }

    /**
     * Returns the {@link Link} with a rel of {@link Link#REL_SELF}.
     */
    @Override
    public Link getId() {
        return linkTo(ReservationController.class)
                .slash("reservations")
                .slash(getIdentity())
                .withSelfRel();
    }
}
