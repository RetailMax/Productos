package com.duoc.productos.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {
    @Test
    void testEqualsAndHashCode() {
        Producto p1 = new Producto();
        p1.setProductId(1L);
        p1.setName("A");
        Producto p2 = new Producto();
        p2.setProductId(1L);
        p2.setName("A");
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        p2.setProductId(2L);
        assertNotEquals(p1, p2);
    }

    @Test
    void testToString() {
        Producto p = new Producto();
        p.setName("Test");
        String s = p.toString();
        assertTrue(s.contains("Test"));
    }

    @Test
    void testGettersAndSetters() {
        Producto p = new Producto();
        p.setProductId(10L);
        p.setName("N");
        p.setDescription("D");
        p.setBrand("B");
        p.setBasePrice(100);
        p.setIsActive(true);
        Categoria c = new Categoria();
        p.setCategoria(c);
        assertEquals(10L, p.getProductId());
        assertEquals("N", p.getName());
        assertEquals("D", p.getDescription());
        assertEquals("B", p.getBrand());
        assertEquals(100, p.getBasePrice());
        assertTrue(p.getIsActive());
        assertEquals(c, p.getCategoria());
    }
} 