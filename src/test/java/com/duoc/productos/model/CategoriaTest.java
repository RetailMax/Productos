package com.duoc.productos.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import com.duoc.productos.model.Producto;
import org.junit.jupiter.api.Tag;

@Tag("unit")
class CategoriaTest {

    @Test
    void testGettersAndSetters() {
        Categoria categoria = new Categoria();
        categoria.setCategoriaId(1L);
        categoria.setName("Tecnología");
        categoria.setDescription("Productos tecnológicos");
        categoria.setIsActive(false);
        LocalDateTime now = LocalDateTime.now();
        categoria.setCreatedAt(now);
        categoria.setProductos(new ArrayList<>());

        assertEquals(1L, categoria.getCategoriaId());
        assertEquals("Tecnología", categoria.getName());
        assertEquals("Productos tecnológicos", categoria.getDescription());
        assertFalse(categoria.getIsActive());
        assertEquals(now, categoria.getCreatedAt());
        assertNotNull(categoria.getProductos());
    }

    @Test
    void testOnCreateSetsCreatedAtAndIsActive() {
        Categoria c = new Categoria();
        c.setIsActive(null);
        c.onCreate();
        assertNotNull(c.getCreatedAt());
        assertTrue(c.getIsActive());
        assertTrue(c.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testOnCreateDoesNotOverrideIsActiveIfSet() {
        Categoria c = new Categoria();
        c.setIsActive(false);
        c.onCreate();
        assertFalse(c.getIsActive());
    }

    @Test
    void testDefaultValues() {
        Categoria categoria = new Categoria();
        assertNull(categoria.getCategoriaId());
        assertNull(categoria.getName());
        assertNull(categoria.getDescription());
        assertTrue(categoria.getIsActive()); // Por defecto true
        assertNull(categoria.getCreatedAt());
        assertNull(categoria.getProductos());
    }

    @Test
    void testSetAndGetProductos() {
        Categoria c = new Categoria();
        Producto p = new Producto();
        p.setName("Prod");
        p.setDescription("Desc");
        p.setBrand("Marca");
        p.setBasePrice(100);
        p.setIsActive(true);
        List<Producto> productos = new ArrayList<>();
        productos.add(p);
        c.setProductos(productos);
        assertEquals(productos, c.getProductos());
    }

    @Test
    void testCanEqual() {
        Categoria c = new Categoria();
        assertTrue(c.canEqual(new Categoria()));
        assertFalse(c.canEqual("otro tipo"));
    }

    @Test
    void testGettersSettersAndToString() {
        Categoria c = new Categoria();
        c.setCategoriaId(1L);
        c.setName("Cat");
        c.setDescription("DescCat");
        c.setIsActive(true);
        assertEquals(1L, c.getCategoriaId());
        assertEquals("Cat", c.getName());
        assertEquals("DescCat", c.getDescription());
        assertTrue(c.getIsActive());
        assertNotNull(c.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        Categoria c1 = new Categoria();
        c1.setCategoriaId(1L);
        c1.setName("Cat");
        c1.setDescription("DescCat");
        c1.setIsActive(true);
        Categoria c2 = new Categoria();
        c2.setCategoriaId(1L);
        c2.setName("Cat");
        c2.setDescription("DescCat");
        c2.setIsActive(true);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        Categoria c3 = new Categoria();
        c3.setCategoriaId(2L);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, null);
        assertNotEquals(c1, "otro tipo");
    }
} 