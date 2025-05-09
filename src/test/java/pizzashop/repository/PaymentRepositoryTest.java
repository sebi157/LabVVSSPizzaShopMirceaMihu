package pizzashop.repository;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;

import java.io.IOException;
import java.util.List;

public class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() throws IOException {
        //arrange
        paymentRepository = new PaymentRepository();
    }

    @Test
    @DisplayName("ECP: Add valid payment (table=1, type=CASH, amount=10.0)")
    void testAddValidPayment() {
        //arrange
        Payment payment = new Payment(1, PaymentType.CASH, 10.0);
        //act
        paymentRepository.add(payment);
        List<Payment> allPayments = paymentRepository.getAll();
        //assert
        assertTrue(allPayments.contains(payment));
    }

    @Test
    @DisplayName("ECP: Invalid table number (0) ⇒ IllegalArgumentException")
    void testAddPaymentInvalidTable() {
        // arrange
        Payment invalidPayment = new Payment(0, PaymentType.CARD, 20.0);
        // act & assert
        assertThrows(IllegalArgumentException.class, () ->
                        paymentRepository.add(invalidPayment),
                "Table 0 is out of valid range [1..8], should throw"
        );
    }

    @Test
    @DisplayName("ECP: Invalid payment type (not CASH/CARD)")
    void testGetPaymentInvalidType() {
        //arrange
        //act & assert
        assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("1,MASTERCARD,100 => No enum constant PaymentType.MASTERCARD");
        });
    }

    @Test
    @DisplayName("ECP: Invalid amount (<=0) ⇒ IllegalArgumentException")
    void testAddPaymentInvalidAmount() {
        // arrange
        Payment payment = new Payment(1, PaymentType.CASH, 0.0);
        // act & assert
        assertThrows(IllegalArgumentException.class, () ->
                        paymentRepository.add(payment),
                "Amount 0.0 is not > 0, should throw"
        );
    }
    //------------------------------

    @Test
    @DisplayName("BVA: TC1_BVA (table=0, type=CASH, amount=0) ⇒ expect error")
    void testTC1_BVA_InvalidTableAndZeroAmount() {
        // table = 0 (just below lower boundary), amount = 0 (lower boundary)
        assertThrows(IllegalArgumentException.class, () ->
                paymentRepository.add(new Payment(0, PaymentType.CASH, 0.0))
        );
    }

    @Test
    @DisplayName("BVA: TC2_BVA (table=9, type=CARD, amount=1) ⇒ expect error")
    void testTC2_BVA_JustBelowUpperTableButMinPositiveAmount() {
        // table = 9 (just below upper boundary 10), amount = 1 (just above zero)
        assertThrows(IllegalArgumentException.class, () ->
                paymentRepository.add(new Payment(9, PaymentType.CARD, 1.0))
        );
    }

    @Test
    @DisplayName("BVA: TC3_BVA (table=1, type=CARD, amount=Double.MAX_VALUE-1) ⇒ valid")
    void testTC3_BVA_UpperAmountMinusOne() {
        // table = 1 (lower boundary), amount = Double.MAX_VALUE - 1
        Payment payment = new Payment(1, PaymentType.CARD, Double.MAX_VALUE - 1);
        paymentRepository.add(payment);
        assertTrue(paymentRepository.getAll().contains(payment));
    }

    @Test
    @DisplayName("BVA: TC4_BVA (table=2, type=CASH, amount=Double.MAX_VALUE) ⇒ valid")
    void testTC4_BVA_MaxDoubleAmount() {
        // table = 2, amount = Double.MAX_VALUE (upper boundary)
        Payment payment = new Payment(2, PaymentType.CASH, Double.MAX_VALUE);
        paymentRepository.add(payment);
        assertTrue(paymentRepository.getAll().contains(payment));
    }
}
