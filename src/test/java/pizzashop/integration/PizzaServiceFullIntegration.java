package pizzashop.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PizzaServiceFullIntegration {

    @TempDir Path tempDir;
    private PizzaService service;
    private File dataFile;

    @BeforeEach
    void init() throws Exception {
        // suprascriem calea fișierului ca să folosim un fișier temporar
        dataFile = tempDir.resolve("payments.txt").toFile();
        dataFile.createNewFile();
        java.lang.reflect.Field f = PaymentRepository.class.getDeclaredField("filename");
        f.setAccessible(true);
        f.set(null, dataFile.getAbsolutePath());

        PaymentRepository repo = new PaymentRepository();
        service = new PizzaService(null, repo);
    }

    @Test
    void addAndGetPaymentsShouldPersistAndRead() {
        service.addPayment(1, PaymentType.CARD, 20.0);
        service.addPayment(3, PaymentType.CASH, 5.0);

        List<Payment> all = service.getPayments();
        assertEquals(2, all.size());
        assertEquals(1, all.get(0).getTableNumber());
        assertEquals(20.0, all.get(0).getAmount());
        assertEquals(PaymentType.CASH, all.get(1).getType());
    }

    @Test
    void getTotalAmountStaticShouldWorkOnPersistedData() {
        service.addPayment(2, PaymentType.CARD, 10.0);
        service.addPayment(4, PaymentType.CARD, 15.0);
        service.addPayment(5, PaymentType.CASH, 7.0);

        List<Payment> all = service.getPayments();
        double totalCard = PizzaService.getTotalAmountStatic(all, PaymentType.CARD);
        assertEquals(25.0, totalCard, 1e-6);
    }
}