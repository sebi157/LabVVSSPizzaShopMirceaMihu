package pizzashop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void constructorAndGettersShouldWork() {
        Payment p = new Payment(3, PaymentType.CASH, 45.5);
        assertEquals(3, p.getTableNumber());
        assertEquals(PaymentType.CASH, p.getType());
        assertEquals(45.5, p.getAmount());
    }

    @Test
    void settersShouldUpdateFields() {
        Payment p = new Payment(1, PaymentType.CARD, 20.0);
        p.setTableNumber(5);
        p.setType(PaymentType.CASH);
        p.setAmount(99.9);
        assertAll(
                () -> assertEquals(5, p.getTableNumber()),
                () -> assertEquals(PaymentType.CASH, p.getType()),
                () -> assertEquals(99.9, p.getAmount())
        );
    }

    @Test
    void toStringShouldReturnCsvFormat() {
        Payment p = new Payment(2, PaymentType.CARD, 10.0);
        assertEquals("2,CARD,10.0", p.toString());
    }
}