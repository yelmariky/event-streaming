package fr.nextdigital.lab.simulator.warehouse.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.nextdigital.lab.simulator.domain.AbstractEntity;
import fr.nextdigital.lab.simulator.domain.Address;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.UriTemplate;

import java.util.ArrayList;
import java.util.List;

public class Warehouse extends AbstractEntity {

    private Long id;
    private List<WarehouseEvent> events = new ArrayList<>();
    private Address address;
    private WarehouseStatus status;

    public Warehouse() {
    }

    public Warehouse(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public WarehouseStatus getStatus() {
        return status;
    }

    public void setStatus(WarehouseStatus status) {
        this.status = status;
    }

    @JsonProperty("warehouseId")
    public Long getIdentity() {
        return id;
    }

    public void setIdentity(Long id) {
        this.id = id;
    }

    public List<WarehouseEvent> getEvents() {
        return events;
    }

    /**
     * Returns the {@link Link} with a rel of {@link Link#REL_SELF}.
     */
    @Override
    public Link getId() {
        return new Link(new UriTemplate("http://warehouse-web/v1/warehouses/{id}")
                .with("id", TemplateVariable.VariableType.PATH_VARIABLE)
                .expand(getIdentity())
                .toString()).withSelfRel();
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", events=" + events +
                ", address=" + address +
                ", status=" + status +
                "} " + super.toString();
    }
}
