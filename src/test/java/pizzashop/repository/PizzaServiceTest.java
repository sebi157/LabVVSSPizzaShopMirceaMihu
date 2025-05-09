package pizzashop.repository;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.service.PizzaService;

class PizzaServiceTest {

    // F02_TC01: l = null, type = CASH → IllegalArgumentException
    @Test
    @DisplayName("F02_TC01: null list, type CASH ⇒ IllegalArgumentException")
    void testF02_TC01_NullListThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> PizzaService.getTotalAmountStatic(null, PaymentType.CASH),
                "Null list should throw");
    }

    // F02_TC02: l = [],      type = CARD → expected total = 0.0
    @Test
    @DisplayName("F02_TC02: empty list, type CARD ⇒ 0.0")
    void testF02_TC02_EmptyListReturnsZero() {
        List<Payment> payments = Collections.emptyList();
        double total = PizzaService.getTotalAmountStatic(payments, PaymentType.CARD);
        assertEquals(0.0, total, "Empty list should yield 0.0");
    }

    // F02_TC03: cannot be tested (null type)
    @Test
    @Disabled("F02_TC03: null PaymentType scenario not testable")
    @DisplayName("F02_TC03: null type ⇒ (cannot test)")
    void testF02_TC03_NullTypeNotTestable() {
        // per spec: “nu se poate testa”
    }

    // F02_TC04: l = [(5, CASH)], type = CARD → expected total = 0.0
    @Test
    @DisplayName("F02_TC04: one CASH payment, type CARD ⇒ 0.0")
    void testF02_TC04_CashOnlyListReturnsZeroForCard() {
        List<Payment> payments = List.of(
                new Payment(1, PaymentType.CASH, 5.0)
        );
        double total = PizzaService.getTotalAmountStatic(payments, PaymentType.CARD);
        assertEquals(0.0, total, "Only CASH payments should not count towards CARD total");
    }

    // F02_TC05: l = [(2, CARD), (3, CARD)], type = CARD → expected total = 5.0
    @Test
    @DisplayName("F02_TC05: two CARD payments ⇒ 5.0")
    void testF02_TC05_TwoCardPaymentsSumCorrectly() {
        List<Payment> payments = List.of(
                new Payment(1, PaymentType.CARD, 2.0),
                new Payment(2, PaymentType.CARD, 3.0)
        );
        double total = PizzaService.getTotalAmountStatic(payments, PaymentType.CARD);
        assertEquals(5.0, total, "Sum of CARD payments 2.0 + 3.0 should be 5.0");
    }
}