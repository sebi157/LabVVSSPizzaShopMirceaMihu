package pizzashop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentRepositoryTest_L4 {

    private PaymentRepository repo;

    @BeforeEach
    void setUp() throws IOException {
        // creare spy și anulare apel real writeAll()
        repo = Mockito.spy(new PaymentRepository());
        doNothing().when(repo).writeAll();
    }

    @Test
    void addNullShouldThrow() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> repo.add(null)
        );
        assertTrue(ex.getMessage().contains("cannot be null"));
    }

    @Test
    void addInvalidTableShouldThrow() {
        Payment bad = new Payment(0, PaymentType.CASH, 10.0);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> repo.add(bad)
        );
        assertTrue(ex.getMessage().contains("Invalid table number"));
    }

    @Test
    void addValidPaymentShouldInvokeWriteAll() {
        Payment good = new Payment(4, PaymentType.CARD, 25.0);
        repo.add(good);
        verify(repo, times(1)).writeAll();
        List<Payment> all = repo.getAll();
        assertTrue(all.contains(good));
    }
}