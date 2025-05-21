package pizzashop.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PizzaServiceIntegrationWithR {

    private PaymentRepository realRepo;
    private PizzaService service;

    @BeforeEach
    void setup() throws Exception {
        // spy real repo, stub writeAll()
        realRepo = spy(new PaymentRepository());
        doNothing().when(realRepo).writeAll();
        service = new PizzaService(null, realRepo);
    }

    @Test
    void addPaymentShouldUseRepoAddAndWriteAll() {
        service.addPayment(2, PaymentType.CASH, 12.0);
        verify(realRepo, times(1)).add(any(Payment.class));
        verify(realRepo, times(1)).writeAll();
    }

    @Test
    void getPaymentsReflectsRepoContent() {
        // mock-uim două obiecte Payment pentru a izola E
        Payment p1 = mock(Payment.class);
        when(p1.getTableNumber()).thenReturn(10);
        when(p1.getType()).thenReturn(PaymentType.CASH);
        when(p1.getAmount()).thenReturn(5.0);
        Payment p2 = mock(Payment.class);
        when(p2.getTableNumber()).thenReturn(11);
        when(p2.getType()).thenReturn(PaymentType.CARD);
        when(p2.getAmount()).thenReturn(7.5);

        doReturn(Arrays.asList(p1, p2)).when(realRepo).getAll();

        List<Payment> got = service.getPayments();
        assertEquals(2, got.size());
        assertSame(p1, got.get(0));
        assertSame(p2, got.get(1));
    }
}