package fr.nextdigital.lab.payment.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.nextdigital.lab.payment.web.action.ConnectOrder;
import fr.nextdigital.lab.payment.web.action.ProcessPayment;
import fr.nextdigital.lab.payment.web.controller.PaymentController;
import fr.nextdigital.lab.domain.Command;
import fr.nextdigital.lab.payment.web.event.PaymentEvent;

import org.springframework.hateoas.Link;

import javax.persistence.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * The {@link Payment} domain object contains information related to a user's payment. The status of an payment is
 * event sourced using events logged to the {@link PaymentEvent} collection attached to this resource.
 *
 * @author Kenny Bastani
 */
@Entity
public class Payment extends AbstractEntity<PaymentEvent, Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;

    private Double amount;
    private Long orderId;

    @Enumerated(value = EnumType.STRING)
    private PaymentMethod paymentMethod;

    public Payment() {
        status = PaymentStatus.PAYMENT_CREATED;
    }

    public Payment(Double amount, PaymentMethod paymentMethod) {
        this();
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    @JsonProperty("paymentId")
    @Override
    public Long getIdentity() {
        return this.id;
    }

    @Override
    public void setIdentity(Long id) {
        this.id = id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Command(method = "connectOrder", controller = PaymentController.class)
    public Payment connectOrder(Long orderId) {
        return getAction(ConnectOrder.class)
                .apply(this, orderId);
    }

    @Command(method = "processPayment", controller = PaymentController.class)
    public Payment processPayment() {
        return getAction(ProcessPayment.class)
                .apply(this);
    }

    /**
     * Returns the {@link Link} with a rel of {@link Link#REL_SELF}.
     */
    @Override
    public Link getId() {
        return linkTo(PaymentController.class)
                .slash("payments")
                .slash(getIdentity())
                .withSelfRel();
    }
}
