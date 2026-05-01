package com.at.shopping.cart;
import com.at.shopping.cart.core.ShoppingCartProcessor;
import com.at.shopping.cart.service.PriceService;
import com.at.shopping.cart.service.TaxService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.at.shopping.cart.model.CartItem;
import com.at.shopping.cart.model.LineItem;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import java.util.Map;
class ShoppingCartProcessorTest {

    private PriceService priceService;
    private TaxService taxService;
    private ShoppingCartProcessor cart;

    @BeforeEach
    void setUp() {
        priceService = mock(PriceService.class);
        taxService = mock(TaxService.class);

        cart = new ShoppingCartProcessor(priceService, taxService);
    }

    // =========================
    // TEST: add item to cart
    // =========================
    @Test
    void shouldAddItemToCartAndIncreaseQuantity() {

        cart.addItem("cheerios", 2);
        cart.addItem("cheerios", 3);

        Map<String, CartItem> items = cart.getItems();

        assertEquals(1, items.size());
        assertEquals(5, items.get("cheerios").getQuantity());
    }

    // =========================
    // TEST: subtotal calculation
    // =========================
    @Test
    void shouldCalculateSubtotalCorrectly() {

        cart.addItem("cheerios", 2);

        when(priceService.getPrice("cheerios")).thenReturn(8.43);

        double subtotal = cart.getSubtotal();

        assertEquals(16.86, subtotal);
        verify(priceService, times(1)).getPrice("cheerios");
    }

    // =========================
    // TEST: tax delegation
    // =========================
    @Test
    void shouldDelegateTaxCalculation() {

        cart.addItem("cheerios", 2);

        when(priceService.getPrice("cheerios")).thenReturn(10.0);
        when(taxService.calculate(20.0)).thenReturn(2.5);

        double tax = cart.getTax();

        assertEquals(2.5, tax);
        verify(taxService).calculate(20.0);
    }

    // =========================
    // TEST: total calculation
    // =========================
    @Test
    void shouldCalculateTotalCorrectly() {

        cart.addItem("cheerios", 1);

        when(priceService.getPrice("cheerios")).thenReturn(10.0);
        when(taxService.calculate(10.0)).thenReturn(1.25);

        double total = cart.getTotal();

        assertEquals(11.25, total);
    }

    // =========================
    // TEST: bill line generation
    // =========================
    @Test
    void shouldGenerateBillLinesCorrectly() {

        cart.addItem("cheerios", 2);

        when(priceService.getPrice("cheerios")).thenReturn(8.0);

        List<LineItem> lines = cart.getBillLines();

        assertEquals(1, lines.size());

        LineItem line = lines.getFirst();

        assertEquals("cheerios", line.name());
        assertEquals(2, line.quantity());
        assertEquals(8.0, line.unitPrice());
        assertEquals(16.0, line.lineTotal());
    }

    // =========================
    // TEST: immutability of cart items
    // =========================
    @Test
    void shouldReturnUnmodifiableCartItems() {

        cart.addItem("cheerios", 1);

        Map<String, CartItem> items = cart.getItems();

        assertThrows(
                UnsupportedOperationException.class,
                () -> items.put("new", new CartItem("new", 1))
        );
    }
}
/*

2. TDD Approach (Step-by-Step)
Step 1: Write Test First

Use JUnit + Mockito

Test: Add item & subtotal

Final Architecture (Simple & Clean)
ShoppingCart
   ↓
PriceService (interface)
   ↓
Mock (tests) / Real API (runtime)
 */