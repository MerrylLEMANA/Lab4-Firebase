package com.example.lab4;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void productGettersAndSetters_workCorrectly() {
        Product product = new Product("id123", "Laptop", 999.99);
        assertEquals("id123", product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(999.99, product.getPrice(), 0.01);
    }

    @Test
    public void productDefaultConstructor_createsEmptyObject() {
        Product product = new Product();
        assertNull(product.getId());
        assertNull(product.getName());
        assertEquals(0.0, product.getPrice(), 0.01);
    }
    // Petit changement pour déclencher le workflow GitHub Actions
}