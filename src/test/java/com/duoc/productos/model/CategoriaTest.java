package com.duoc.productos.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

class CategoriaTest {

    @Test
    void testEqualsAndHashCode() {
        Categoria cat1 = new Categoria();
        cat1.setCategoriaId(1L);
        cat1.setName("Test");
        cat1.setDescription("Desc");
        cat1.setIsActive(true);
        cat1.setCreatedAt(LocalDateTime.now());

        Categoria cat2 = new Categoria();
        cat2.setCategoriaId(1L);
        cat2.setName("Test");
        cat2.setDescription("Desc");
        cat2.setIsActive(true);
        cat2.setCreatedAt(cat1.getCreatedAt());

        // Test equals
        assertEquals(cat1, cat2);
        assertEquals(cat2, cat1);
        
        // Test hashCode
        assertEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testEqualsWithDifferentIds() {
        Categoria cat1 = new Categoria();
        cat1.setCategoriaId(1L);
        cat1.setName("Test");

        Categoria cat2 = new Categoria();
        cat2.setCategoriaId(2L);
        cat2.setName("Test");

        assertNotEquals(cat1, cat2);
        assertNotEquals(cat2, cat1);
        assertNotEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testEqualsWithNullId() {
        Categoria cat1 = new Categoria();
        cat1.setCategoriaId(null);
        cat1.setName("Test");

        Categoria cat2 = new Categoria();
        cat2.setCategoriaId(1L);
        cat2.setName("Test");

        assertNotEquals(cat1, cat2);
        assertNotEquals(cat2, cat1);
    }

    @Test
    void testEqualsWithBothNullIds() {
        Categoria cat1 = new Categoria();
        cat1.setCategoriaId(null);
        cat1.setName("Test");

        Categoria cat2 = new Categoria();
        cat2.setCategoriaId(null);
        cat2.setName("Test");

        assertEquals(cat1, cat2);
        assertEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testEqualsWithDifferentObject() {
        Categoria cat = new Categoria();
        cat.setCategoriaId(1L);
        
        assertNotEquals(cat, "not a categoria");
        assertNotEquals(cat, null);
    }

    @Test
    void testEqualsWithSameObject() {
        Categoria cat = new Categoria();
        cat.setCategoriaId(1L);
        
        assertEquals(cat, cat);
    }

    @Test
    void testHashCodeConsistency() {
        Categoria cat = new Categoria();
        cat.setCategoriaId(1L);
        cat.setName("Test");
        cat.setDescription("Desc");
        cat.setIsActive(true);
        cat.setCreatedAt(LocalDateTime.now());

        int hashCode1 = cat.hashCode();
        int hashCode2 = cat.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeWithNullValues() {
        Categoria cat = new Categoria();
        cat.setCategoriaId(null);
        cat.setName(null);
        cat.setDescription(null);
        cat.setIsActive(null);
        cat.setCreatedAt(null);

        // Should not throw exception
        assertDoesNotThrow(() -> cat.hashCode());
    }

    @Test
    void testToString() {
        Categoria cat = new Categoria();
        cat.setCategoriaId(1L);
        cat.setName("Test");
        cat.setDescription("Desc");
        cat.setIsActive(true);

        String result = cat.toString();
        assertTrue(result.contains("Test"));
        assertTrue(result.contains("Desc"));
    }

    @Test
    void testGettersAndSetters() {
        Categoria cat = new Categoria();
        LocalDateTime now = LocalDateTime.now();

        cat.setCategoriaId(1L);
        cat.setName("Test");
        cat.setDescription("Desc");
        cat.setIsActive(true);
        cat.setImageUrl("http://example.com/image.jpg");
        cat.setCreatedAt(now);

        assertEquals(1L, cat.getCategoriaId());
        assertEquals("Test", cat.getName());
        assertEquals("Desc", cat.getDescription());
        assertTrue(cat.getIsActive());
        assertEquals("http://example.com/image.jpg", cat.getImageUrl());
        assertEquals(now, cat.getCreatedAt());
    }

    @Test
    void testOnCreate() {
        Categoria cat = new Categoria();
        cat.setIsActive(null);
        
        cat.onCreate();
        
        assertNotNull(cat.getCreatedAt());
        assertTrue(cat.getIsActive());
    }

    @Test
    void testOnCreateWithExistingIsActive() {
        Categoria cat = new Categoria();
        cat.setIsActive(false);
        
        cat.onCreate();
        
        assertNotNull(cat.getCreatedAt());
        assertFalse(cat.getIsActive()); // Should not change existing value
    }

    @Test
    void testSetProductos() {
        Categoria cat = new Categoria();
        List<Producto> productos = new ArrayList<>();
        
        // Crear productos de prueba
        Producto producto1 = new Producto();
        producto1.setProductId(1L);
        producto1.setName("Producto 1");
        producto1.setDescription("Descripción del producto 1");
        producto1.setBrand("Marca 1");
        producto1.setBasePrice(100);
        producto1.setIsActive(true);
        
        Producto producto2 = new Producto();
        producto2.setProductId(2L);
        producto2.setName("Producto 2");
        producto2.setDescription("Descripción del producto 2");
        producto2.setBrand("Marca 2");
        producto2.setBasePrice(200);
        producto2.setIsActive(true);
        
        productos.add(producto1);
        productos.add(producto2);
        
        // Probar setProductos
        cat.setProductos(productos);
        
        // Verificar que se estableció correctamente
        assertNotNull(cat.getProductos());
        assertEquals(2, cat.getProductos().size());
        assertEquals("Producto 1", cat.getProductos().get(0).getName());
        assertEquals("Producto 2", cat.getProductos().get(1).getName());
        assertEquals("Marca 1", cat.getProductos().get(0).getBrand());
        assertEquals("Marca 2", cat.getProductos().get(1).getBrand());
    }

    @Test
    void testSetProductosWithNull() {
        Categoria cat = new Categoria();
        
        // Probar setProductos con null
        cat.setProductos(null);
        
        // Verificar que se estableció null
        assertNull(cat.getProductos());
    }

    @Test
    void testSetProductosWithEmptyList() {
        Categoria cat = new Categoria();
        List<Producto> productosVacios = new ArrayList<>();
        
        // Probar setProductos con lista vacía
        cat.setProductos(productosVacios);
        
        // Verificar que se estableció la lista vacía
        assertNotNull(cat.getProductos());
        assertTrue(cat.getProductos().isEmpty());
    }

    @Test
    void testSetProductosAndGetProductos() {
        Categoria cat = new Categoria();
        List<Producto> productos = new ArrayList<>();
        
        Producto producto = new Producto();
        producto.setProductId(1L);
        producto.setName("Test Product");
        producto.setDescription("Test Description");
        producto.setBrand("Test Brand");
        producto.setBasePrice(150);
        producto.setIsActive(true);
        
        productos.add(producto);
        
        // Establecer productos
        cat.setProductos(productos);
        
        // Obtener productos y verificar
        List<Producto> productosObtenidos = cat.getProductos();
        assertNotNull(productosObtenidos);
        assertEquals(1, productosObtenidos.size());
        assertEquals("Test Product", productosObtenidos.get(0).getName());
        assertEquals("Test Description", productosObtenidos.get(0).getDescription());
        assertEquals("Test Brand", productosObtenidos.get(0).getBrand());
        assertEquals(150, productosObtenidos.get(0).getBasePrice());
        assertTrue(productosObtenidos.get(0).getIsActive());
    }

    @Test
    void testEqualsWithDifferentTypes() {
        Categoria cat = new Categoria();
        cat.setCategoriaId(1L);
        
        // Probar con diferentes tipos de objetos
        assertNotEquals(cat, new Object());
        assertNotEquals(cat, "string");
        assertNotEquals(cat, 123);
        assertNotEquals(cat, null);
    }

    @Test
    void testEqualsWithDifferentFields() {
        Categoria cat1 = new Categoria();
        cat1.setCategoriaId(1L);
        cat1.setName("Test");
        cat1.setDescription("Desc");
        cat1.setIsActive(true);
        cat1.setImageUrl("http://example.com/image.jpg");
        cat1.setCreatedAt(LocalDateTime.now());
        
        Categoria cat2 = new Categoria();
        cat2.setCategoriaId(1L);
        cat2.setName("Different"); // Campo diferente
        cat2.setDescription("Desc");
        cat2.setIsActive(true);
        cat2.setImageUrl("http://example.com/image.jpg");
        cat2.setCreatedAt(cat1.getCreatedAt());
        
        // Deberían ser diferentes porque Lombok compara todos los campos
        assertNotEquals(cat1, cat2);
    }

    @Test
    void testEqualsWithNullFields() {
        Categoria cat1 = new Categoria();
        cat1.setCategoriaId(1L);
        cat1.setName(null);
        cat1.setDescription(null);
        cat1.setIsActive(null);
        cat1.setImageUrl(null);
        cat1.setCreatedAt(null);
        
        Categoria cat2 = new Categoria();
        cat2.setCategoriaId(1L);
        cat2.setName("Test"); // Campo diferente
        cat2.setDescription("Desc");
        cat2.setIsActive(true);
        cat2.setImageUrl("http://example.com/image.jpg");
        cat2.setCreatedAt(LocalDateTime.now());
        
        // Deberían ser diferentes porque tienen campos diferentes
        assertNotEquals(cat1, cat2);
    }

    @Test
    void testEqualsWithAllNullFields() {
        Categoria cat1 = new Categoria();
        cat1.setCategoriaId(null);
        cat1.setName(null);
        cat1.setDescription(null);
        cat1.setIsActive(null);
        cat1.setImageUrl(null);
        cat1.setCreatedAt(null);
        
        Categoria cat2 = new Categoria();
        cat2.setCategoriaId(null);
        cat2.setName(null);
        cat2.setDescription(null);
        cat2.setIsActive(null);
        cat2.setImageUrl(null);
        cat2.setCreatedAt(null);
        
        // Deberían ser iguales porque todos los campos son null
        assertEquals(cat1, cat2);
        assertEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testEqualsWithMixedNullAndNonNullFields() {
        Categoria cat1 = new Categoria();
        cat1.setCategoriaId(1L);
        cat1.setName("Test");
        cat1.setDescription(null);
        cat1.setIsActive(true);
        cat1.setImageUrl(null);
        cat1.setCreatedAt(LocalDateTime.now());
        
        Categoria cat2 = new Categoria();
        cat2.setCategoriaId(1L);
        cat2.setName("Test");
        cat2.setDescription("Different"); // Campo diferente
        cat2.setIsActive(false); // Campo diferente
        cat2.setImageUrl("http://example.com/image.jpg"); // Campo diferente
        cat2.setCreatedAt(cat1.getCreatedAt());
        
        // Deberían ser diferentes porque tienen campos diferentes
        assertNotEquals(cat1, cat2);
    }

    @Test
    void testHashCodeWithDifferentObjects() {
        Categoria cat1 = new Categoria();
        cat1.setCategoriaId(1L);
        cat1.setName("Test");
        cat1.setDescription("Desc");
        cat1.setIsActive(true);
        cat1.setImageUrl("http://example.com/image.jpg");
        cat1.setCreatedAt(LocalDateTime.now());
        
        Categoria cat2 = new Categoria();
        cat2.setCategoriaId(2L);
        cat2.setName("Test");
        cat2.setDescription("Desc");
        cat2.setIsActive(true);
        cat2.setImageUrl("http://example.com/image.jpg");
        cat2.setCreatedAt(cat1.getCreatedAt());
        
        // Diferentes IDs deberían producir diferentes hashCodes
        assertNotEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testHashCodeWithSameValues() {
        Categoria cat1 = new Categoria();
        cat1.setCategoriaId(1L);
        cat1.setName("Test");
        cat1.setDescription("Desc");
        cat1.setIsActive(true);
        cat1.setImageUrl("http://example.com/image.jpg");
        cat1.setCreatedAt(LocalDateTime.now());
        
        Categoria cat2 = new Categoria();
        cat2.setCategoriaId(1L);
        cat2.setName("Test");
        cat2.setDescription("Desc");
        cat2.setIsActive(true);
        cat2.setImageUrl("http://example.com/image.jpg");
        cat2.setCreatedAt(cat1.getCreatedAt());
        
        // Mismos valores deberían producir el mismo hashCode
        assertEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testHashCodeWithPartialNullValues() {
        Categoria cat1 = new Categoria();
        cat1.setCategoriaId(1L);
        cat1.setName("Test");
        cat1.setDescription(null);
        cat1.setIsActive(true);
        cat1.setImageUrl(null);
        cat1.setCreatedAt(LocalDateTime.now());
        
        Categoria cat2 = new Categoria();
        cat2.setCategoriaId(1L);
        cat2.setName("Test");
        cat2.setDescription("Different"); // Campo diferente
        cat2.setIsActive(false); // Campo diferente
        cat2.setImageUrl("http://example.com/image.jpg"); // Campo diferente
        cat2.setCreatedAt(cat1.getCreatedAt());
        
        // Deberían tener hashCodes diferentes porque tienen campos diferentes
        assertNotEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testHashCodeWithAllFieldsSet() {
        Categoria cat = new Categoria();
        cat.setCategoriaId(1L);
        cat.setName("Test");
        cat.setDescription("Desc");
        cat.setIsActive(true);
        cat.setImageUrl("http://example.com/image.jpg");
        cat.setCreatedAt(LocalDateTime.now());
        
        // Verificar que no lanza excepción con todos los campos seteados
        assertDoesNotThrow(() -> cat.hashCode());
        assertNotNull(cat.hashCode());
    }

    @Test
    void testEqualsWithIdenticalObjects() {
        LocalDateTime now = LocalDateTime.now();
        
        Categoria cat1 = new Categoria();
        cat1.setCategoriaId(1L);
        cat1.setName("Test");
        cat1.setDescription("Desc");
        cat1.setIsActive(true);
        cat1.setImageUrl("http://example.com/image.jpg");
        cat1.setCreatedAt(now);
        
        Categoria cat2 = new Categoria();
        cat2.setCategoriaId(1L);
        cat2.setName("Test");
        cat2.setDescription("Desc");
        cat2.setIsActive(true);
        cat2.setImageUrl("http://example.com/image.jpg");
        cat2.setCreatedAt(now);
        
        // Deberían ser iguales porque todos los campos son idénticos
        assertEquals(cat1, cat2);
        assertEquals(cat1.hashCode(), cat2.hashCode());
    }
} 