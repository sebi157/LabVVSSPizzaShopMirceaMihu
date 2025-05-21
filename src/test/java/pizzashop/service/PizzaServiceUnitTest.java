package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PizzaServiceUnitTest {

    private PaymentRepository payRepo;
    private MenuRepository menuRepo;
    private PizzaService service;

    @BeforeEach
    void init() {
        payRepo  = Mockito.mock(PaymentRepository.class);
        menuRepo = Mockito.mock(MenuRepository.class);
        service  = new PizzaService(menuRepo, payRepo);
    }

    @Test
    void getPaymentsShouldDelegateToRepo() {
        when(payRepo.getAll()).thenReturn(Arrays.asList(
                new Payment(1, PaymentType.CARD, 10.0),
                new Payment(2, PaymentType.CASH, 5.0)
        ));
        List<Payment> list = service.getPayments();
        assertEquals(2, list.size());
        verify(payRepo, times(1)).getAll();
    }

    @Test
    void addPaymentShouldCreateAndStore() {
        // stub writeAll in repo so no real I/O
        doNothing().when(payRepo).add(any());
        service.addPayment(7, PaymentType.CARD, 30.0);
        // capturăm obiectul creat și trimis la repo
        verify(payRepo).add(argThat(
                p -> p.getTableNumber()==7
                        && p.getType()==PaymentType.CARD
                        && p.getAmount()==30.0
        ));
    }

    @Test
    void getTotalAmountStaticShouldSumByType() {
        List<Payment> data = Arrays.asList(
                new Payment(1, PaymentType.CARD, 10.0),
                new Payment(2, PaymentType.CARD, 5.5),
                new Payment(3, PaymentType.CASH, 3.0)
        );
        double total = PizzaService.getTotalAmountStatic(data, PaymentType.CARD);
        assertEquals(15.5, total, 1e-6);
    }

    @Test
    void getTotalAmountStaticNullListShouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> PizzaService.getTotalAmountStatic(null, PaymentType.CARD));
    }
}