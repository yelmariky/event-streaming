package fr.nextdigital.lab.order.web.warehouse.domain;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;

import fr.nextdigital.lab.order.web.domain.Order;

public class Warehouses extends Resources<Order> {

    /**
     * Creates an empty {@link Resources} instance.
     */
    public Warehouses() {
    }

    /**
     * Creates a {@link Resources} instance with the given content and {@link Link}s (optional).
     *
     * @param content must not be {@literal null}.
     * @param links   the links to be added to the {@link Resources}.
     */
    public Warehouses(Iterable<Order> content, Link... links) {
        super(content, links);
    }
}
