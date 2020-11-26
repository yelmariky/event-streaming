package fr.nextdigital.lab.payment.web;

import fr.nextdigital.lab.payment.web.domain.Payment;
import fr.nextdigital.lab.payment.web.domain.PaymentMethod;
import fr.nextdigital.lab.payment.web.domain.PaymentService;
import fr.nextdigital.lab.event.EventService;
import fr.nextdigital.lab.payment.web.event.PaymentEvent;
import fr.nextdigital.lab.payment.web.repository.PaymentRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class PaymentServiceTests {

    @MockBean
    private EventService<PaymentEvent, Long> eventService;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private DiscoveryClient discoveryClient;

    private PaymentService paymentService;

    @Before
    public void before() {
        paymentService = new PaymentService(paymentRepository, eventService);
    }

    @Test
    public void getPaymentReturnsPayment() throws Exception {
         Payment payment = new Payment(42.0, PaymentMethod.CREDIT_CARD);
         Optional<Payment> expected = Optional.of(payment);

        given(this.paymentRepository.findById(1L)).willReturn(expected);

        Payment actual = paymentService.get(1L);

        assertThat(actual).isNotNull();
        assertThat(actual.getPaymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
        assertThat(actual.getAmount()).isEqualTo(42.0);
    }
}