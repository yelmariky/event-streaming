package fr.nextdigital.lab.simulator.inventory.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.nextdigital.lab.simulator.domain.AbstractEntity;

import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

public class Inventory extends AbstractEntity {
    private Long id;
    private InventoryStatus status;
    private List<InventoryEvent> events = new ArrayList<>();
    private String productId;

    public Inventory() {
    }

    public Inventory(InventoryStatus status, String productId) {
        this.status = status;
        this.productId = productId;
    }

    @JsonProperty("inventoryId")
    public Long getIdentity() {
        return this.id;
    }

    public void setIdentity(Long id) {
        this.id = id;
    }

    public InventoryStatus getStatus() {
        return status;
    }

    public void setStatus(InventoryStatus status) {
        this.status = status;
    }

    public List<InventoryEvent> getEvents() {
        return events;
    }

    public void setEvents(List<InventoryEvent> events) {
        this.events = events;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Returns the {@link Link} with a rel of {@link Link#REL_SELF}.
     */
    @Override
    public Link getId() {
        return getLink("self");
    }
}
