package com.duoc.productos.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class ProductoTest {
    @Test
    void testGettersSettersAndToString() {
        Producto p = new Producto();
        p.setProductId(1L);
        p.setName("Test");
        p.setDescription("Desc");
        p.setBrand("Marca");
        p.setBasePrice(100);
        p.setIsActive(true);
        p.setCategoria(new Categoria());
        assertEquals(1L, p.getProductId());
        assertEquals("Test", p.getName());
        assertEquals("Desc", p.getDescription());
        assertEquals("Marca", p.getBrand());
        assertEquals(100, p.getBasePrice());
        assertTrue(p.getIsActive());
        assertNotNull(p.getCategoria());
        assertNotNull(p.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        Producto p1 = new Producto();
        p1.setProductId(1L);
        Producto p2 = new Producto();
        p2.setProductId(1L);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testPrePersistSetsCreatedAtAndIsActive() {
        Producto p = new Producto();
        p.setIsActive(null);
        p.onCreate();
        assertNotNull(p.getCreatedAt());
        assertTrue(p.getIsActive());
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

    @Test
    void testConstructorCompleto() {
        LocalDateTime fecha = LocalDateTime.now();
        Categoria cat = new Categoria();
        Producto p = new Producto(1L, "A", "B", "C", 10, true, fecha, cat);
        assertEquals(1L, p.getProductId());
        assertEquals("A", p.getName());
        assertEquals("B", p.getDescription());
        assertEquals("C", p.getBrand());
        assertEquals(10, p.getBasePrice());
        assertTrue(p.getIsActive());
        assertEquals(fecha, p.getCreatedAt());
        assertEquals(cat, p.getCategoria());
    }

    @Test
    void testEqualsAndHashCode_variosCasos() {
        Categoria cat = new Categoria();
        LocalDateTime fecha = LocalDateTime.now();
        Producto p1 = new Producto(1L, "A", "B", "C", 10, true, fecha, cat);
        Producto p2 = new Producto(1L, "A", "B", "C", 10, true, fecha, cat);
        Producto p3 = new Producto(2L, "X", "Y", "Z", 20, false, fecha, null);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, p3);
        assertNotEquals(p1, null);
        assertNotEquals(p1, "otro tipo");
    }

    @Test
    void testEqualsWithNullFields() {
        Producto p1 = new Producto(null, null, null, null, null, null, null, null);
        Producto p2 = new Producto(null, null, null, null, null, null, null, null);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
} 