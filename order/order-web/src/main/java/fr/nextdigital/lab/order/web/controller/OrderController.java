package fr.nextdigital.lab.order.web.controller;

import fr.nextdigital.lab.order.web.domain.Order;
import fr.nextdigital.lab.order.web.domain.OrderService;
import fr.nextdigital.lab.order.web.domain.OrderStatus;
import fr.nextdigital.lab.event.EventService;
import fr.nextdigital.lab.event.Events;
import fr.nextdigital.lab.order.web.event.OrderEvent;
import fr.nextdigital.lab.order.web.reservation.domain.Reservations;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/v1")
public class OrderController {

    private final OrderService orderService;
    private final EventService<OrderEvent, Long> eventService;
    private final DiscoveryClient discoveryClient;

    public OrderController(OrderService orderService, EventService<OrderEvent, Long> eventService, DiscoveryClient
            discoveryClient) {
        this.orderService = orderService;
        this.eventService = eventService;
        this.discoveryClient = discoveryClient;
    }

    @PostMapping(path = "/orders")
    public ResponseEntity createOrder(@RequestBody Order order) {
        return Optional.ofNullable(createOrderResource(order))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException("Order creation failed"));
    }

    @PutMapping(path = "/orders/{id}")
    public ResponseEntity updateOrder(@RequestBody Order order, @PathVariable Long id) {
        return Optional.ofNullable(updateOrderResource(id, order))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Order update failed"));
    }

    @RequestMapping(path = "/orders/{id}")
    public ResponseEntity getOrder(@PathVariable Long id) {
        return Optional.ofNullable(getOrderResource(orderService.get(id)))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/orders/{id}")
    public ResponseEntity deleteOrder(@PathVariable Long id) {
        return Optional.ofNullable(orderService.get(id).delete())
                .map(e -> new ResponseEntity<>(HttpStatus.NO_CONTENT))
                .orElseThrow(() -> new RuntimeException("Order deletion failed"));
    }

    @RequestMapping(path = "/orders/{id}/events")
    public ResponseEntity getOrderEvents(@PathVariable Long id) {
        return Optional.of(getOrderEventResources(id))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Could not get order events"));
    }

    @RequestMapping(path = "/orders/{id}/events/{eventId}")
    public ResponseEntity getOrderEvent(@PathVariable Long id, @PathVariable Long eventId) {
        return Optional.of(getEventResource(eventId))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Could not get order events"));
    }

    @PostMapping(path = "/orders/{id}/events")
    public ResponseEntity createOrder(@PathVariable Long id, @RequestBody OrderEvent event) {
        return Optional.ofNullable(appendEventResource(id, event))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException("Append order event failed"));
    }

    @RequestMapping(path = "/orders/{id}/commands")
    public ResponseEntity getCommands(@PathVariable Long id) {
        return Optional.ofNullable(getCommandsResources(id))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The order could not be found"));
    }

    @RequestMapping(path = "/orders/{id}/commands/connectAccount")
    public ResponseEntity connectAccount(@PathVariable Long id, @RequestParam(value = "accountId") Long accountId) {
        return Optional.ofNullable(orderService.get(id)
                .connectAccount(accountId))
                .map(e -> new ResponseEntity<>(getOrderResource(e), HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/orders/{id}/commands/connectPayment")
    public ResponseEntity connectPayment(@PathVariable Long id, @RequestParam(value = "paymentId") Long paymentId) {
        return Optional.ofNullable(orderService.get(id)
                .connectPayment(paymentId))
                .map(e -> new ResponseEntity<>(getOrderResource(e), HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/orders/{id}/commands/createPayment")
    public ResponseEntity createPayment(@PathVariable Long id) {
        return Optional.of(orderService.get(id))
                .map(Order::createPayment)
                .map(this::getOrderResource)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/orders/{id}/commands/processPayment")
    public ResponseEntity processPayment(@PathVariable Long id) {
        return Optional.ofNullable(orderService.get(id)
                .processPayment())
                .map(e -> new ResponseEntity<>(getOrderResource(e), HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/orders/{id}/commands/reserveInventory")
    public ResponseEntity reserveInventory(@PathVariable Long id) {
        return Optional.ofNullable(orderService.get(id).reserveInventory())
                .map(e -> new ResponseEntity<>(getOrderResource(e), HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/orders/{id}/commands/addReservation")
    public ResponseEntity addReservation(@PathVariable Long id, @RequestParam(value = "reservationId") Long
            reservationId) {
        return Optional.ofNullable(orderService.get(id).addReservation(reservationId))
                .map(e -> new ResponseEntity<>(getOrderResource(e), HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/orders/{id}/commands/completeReservation")
    public ResponseEntity completeReservation(@PathVariable Long id) {
        return Optional.ofNullable(orderService.get(id).completeReservation())
                .map(e -> new ResponseEntity<>(getOrderResource(e), HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/orders/{id}/commands/completeOrder")
    public ResponseEntity completeOrder(@PathVariable Long id) {
        return Optional.ofNullable(orderService.get(id).completeOrder())
                .map(e -> new ResponseEntity<>(getOrderResource(e), HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/order/{id}/commands/updateOrderStatus")
    public ResponseEntity updateOrderStatus(@PathVariable Long id, @RequestParam(value = "status") OrderStatus status) {
        return Optional.ofNullable(orderService.get(id).updateOrderStatus(status))
                .map(e -> new ResponseEntity<>(getOrderResource(e), HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/orders/search/findOrdersByAccountId")
    public ResponseEntity findOrdersByAccountId(@RequestParam("accountId") Long accountId) {
        return Optional.ofNullable(orderService.findOrdersByAccountId(accountId))
                .map(e -> new ResponseEntity<>(new Resources<Order>(e), HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/orders/{id}/reservations")
    public ResponseEntity getOrderReservations(@PathVariable Long id) {
        return Optional.of(getOrderReservationsResource(id))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Could not get order reservations"));
    }

    private Reservations getOrderReservationsResource(Long orderId) {
        Order order = orderService.get(orderId);
        Assert.notNull(order, "Order could not be found");

        Reservations orderReservations = order.getReservations();

        orderReservations.add(
                linkTo(OrderController.class)
                        .slash("orders")
                        .slash(orderId)
                        .slash("reservations")
                        .withSelfRel(),
                linkTo(OrderController.class)
                        .slash("orders")
                        .slash(orderId)
                        .withRel("order")
        );

        return orderReservations;
    }

    /**
     * Creates a new {@link Order} entity and persists the result to the repository.
     *
     * @param order is the {@link Order} model used to create a new order
     * @return a hypermedia resource for the newly created {@link Order}
     */
    private Resource<Order> createOrderResource(Order order) {
        Assert.notNull(order, "Order body must not be null");

        // Create the new order
        order = orderService.registerOrder(order);

        return getOrderResource(order);
    }

    /**
     * Update a {@link Order} entity for the provided identifier.
     *
     * @param id    is the unique identifier for the {@link Order} update
     * @param order is the entity representation containing any updated {@link Order} fields
     * @return a hypermedia resource for the updated {@link Order}
     */
    private Resource<Order> updateOrderResource(Long id, Order order) {
        order.setIdentity(id);
        return getOrderResource(orderService.update(order));
    }

    /**
     * Appends an {@link OrderEvent} domain event to the event log of the {@link Order} aggregate with the
     * specified orderId.
     *
     * @param orderId is the unique identifier for the {@link Order}
     * @param event   is the {@link OrderEvent} that attempts to alter the state of the {@link Order}
     * @return a hypermedia resource for the newly appended {@link OrderEvent}
     */
    private Resource<OrderEvent> appendEventResource(Long orderId, OrderEvent event) {
        Resource<OrderEvent> eventResource = null;

        orderService.get(orderId)
                .sendAsyncEvent(event);

        if (event != null) {
            eventResource = new Resource<>(event,
                    linkTo(OrderController.class)
                            .slash("orders")
                            .slash(orderId)
                            .slash("events")
                            .slash(event.getEventId())
                            .withSelfRel(),
                    linkTo(OrderController.class)
                            .slash("orders")
                            .slash(orderId)
                            .withRel("order")
            );
        }

        return eventResource;
    }

    private OrderEvent getEventResource(Long eventId) {
        return eventService.findOne(eventId);
    }

    private Events getOrderEventResources(Long id) {
        return eventService.find(id);
    }

    private LinkBuilder linkBuilder(String name, Long id) {
        Method method;

        try {
            method = OrderController.class.getMethod(name, Long.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return linkTo(OrderController.class, method, id);
    }

    /**
     * Get a hypermedia enriched {@link Order} entity.
     *
     * @param order is the {@link Order} to enrich with hypermedia links
     * @return is a hypermedia enriched resource for the supplied {@link Order} entity
     */
    private Resource<Order> getOrderResource(Order order) {
        if (order == null) return null;

        if (!order.hasLink("commands")) {
            // Add command link
            order.add(linkBuilder("getCommands", order.getIdentity()).withRel("commands"));
        }

        if (!order.hasLink("events")) {
            // Add get events link
            order.add(linkBuilder("getOrderEvents", order.getIdentity()).withRel("events"));
        }

        if (!order.hasLink("reservations")) {
            // Add get reservations link
            order.add(linkBuilder("getOrderReservations", order.getIdentity()).withRel("reservations"));
        }

        // Add remote account link
        if (order.getAccountId() != null && !order.hasLink("account")) {
            Link result = getRemoteLink("account-web", "/v1/accounts/{id}", order.getAccountId(), "account");
            if (result != null)
                order.add(result);
        }

        // Add remote payment link
        if (order.getPaymentId() != null && !order.hasLink("payment")) {
            Link result = getRemoteLink("payment-web", "/v1/payments/{id}", order.getPaymentId(), "payment");
            if (result != null)
                order.add(result);
        }

        return new Resource<>(order);
    }

    private Link getRemoteLink(String service, String relative, Object identifier, String rel) {
        Link result = null;
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(service);
        if (serviceInstances.size() > 0) {
            ServiceInstance serviceInstance = serviceInstances.get(new Random().nextInt(serviceInstances.size()));
            result = new Link(new UriTemplate(serviceInstance.getUri()
                    .toString()
                    .concat(relative)).with("id", TemplateVariable.VariableType.PATH_VARIABLE)
                    .expand(identifier)
                    .toString())
                    .withRel(rel);
        }
        return result;
    }

    private ResourceSupport getCommandsResources(Long id) {
        Order order = new Order();
        order.setIdentity(id);
        return new Resource<>(order.getCommands());
    }
}
