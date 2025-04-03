package pizzashop.repository;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import pizzashop.repository.MenuRepository;
import pizzashop.model.Order;

import java.util.List;

public class MenuRepositoryTest {

    private MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        //arrange
        menuRepository = new MenuRepository();
    }

    @Test
    @DisplayName("Read menu - valid items in file")
    void testGetMenu_ValidFile() {
        //act
        List<Order> menu = menuRepository.getMenu();
        //assert
        assertNotNull(menu);
        assertFalse(menu.isEmpty());
    }

    @Test
    @DisplayName("Invalid line in menu file - should throw IllegalArgumentException")
    void testGetMenu_InvalidLine() {
        //arrange
        //act & assert
        assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("PizzaWithoutPrice => No token for price");
        });
    }
}
