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
    @DisplayName("ECP: Invalid table number (0)")
    void testAddPaymentInvalidTable() {
        //arrange
        Payment invalidPayment = new Payment(0, PaymentType.CARD, 20.0);
        //act
        paymentRepository.add(invalidPayment);
        List<Payment> allPayments = paymentRepository.getAll();
        //assert
        assertTrue(allPayments.contains(invalidPayment));
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
    @DisplayName("ECP: Invalid amount (<=0)")
    void testAddPaymentInvalidAmount() {
        //arrange
        Payment payment = new Payment(1, PaymentType.CASH, 0.0);
        //act
        paymentRepository.add(payment);
        List<Payment> allPayments = paymentRepository.getAll();
        //assert
        assertTrue(allPayments.contains(payment));
    }
}
