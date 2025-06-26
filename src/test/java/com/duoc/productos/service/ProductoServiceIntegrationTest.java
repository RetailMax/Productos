package com.duoc.productos.service;

import com.duoc.productos.model.Categoria;
import com.duoc.productos.model.Producto;
import com.duoc.productos.repository.CategoriaRepository;
import com.duoc.productos.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductoServiceIntegrationTest {
    @Autowired
    ProductoService productoService;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;

    Categoria categoria;
    Producto producto;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setName("TestCat");
        categoria.setDescription("Desc");
        categoria.setIsActive(true);
        categoria = categoriaRepository.save(categoria);

        producto = new Producto();
        producto.setName("Prod1");
        producto.setDescription("Desc1");
        producto.setBrand("Marca1");
        producto.setBasePrice(100);
        producto.setIsActive(true);
        producto.setCategoria(categoria);
        producto = productoService.save(producto);
    }

    @Test
    void testSaveAndFindById() {
        Optional<Producto> found = productoService.findById(producto.getProductId());
        assertTrue(found.isPresent());
        assertEquals("Prod1", found.get().getName());
    }

    @Test
    void testActivarProducto() {
        producto.setIsActive(false);
        productoRepository.save(producto);
        boolean result = productoService.activarProducto(producto.getProductId());
        assertTrue(result);
        Producto actualizado = productoRepository.findById(producto.getProductId()).get();
        assertTrue(actualizado.getIsActive());
    }

    @Test
    void testDeleteById() {
        Long id = producto.getProductId();
        productoService.deleteById(id);
        Optional<Producto> eliminado = productoRepository.findById(id);
        assertTrue(eliminado.isPresent());
        assertFalse(eliminado.get().getIsActive());
    }

    @Test
    void testSearchProductos() {
        List<Producto> result = productoService.searchProductos("Prod1");
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByCategoriaPaged() {
        var page = productoService.findByCategoriaPaged(categoria.getCategoriaId(), PageRequest.of(0, 10));
        assertFalse(page.isEmpty());
    }

    @Test
    void testFindByIdNotFound() {
        assertTrue(productoService.findById(-1L).isEmpty());
    }

    @Test
    void testExistsById() {
        assertTrue(productoService.existsById(producto.getProductId()));
        assertFalse(productoService.existsById(-1L));
    }

    @Test
    void testFindByCategoria() {
        List<Producto> result = productoService.findByCategoria(categoria.getCategoriaId());
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindAllActivePaged() {
        var page = productoService.findAllActivePaged(PageRequest.of(0, 10));
        assertFalse(page.isEmpty());
    }

    @Test
    void testExistsByName() {
        assertTrue(productoService.existsByName("Prod1"));
        assertFalse(productoService.existsByName("NoExiste"));
    }

    @Test
    void testFindAll() {
        List<Producto> result = productoService.findAll();
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindInactivos() {
        producto.setIsActive(false);
        productoRepository.save(producto);
        List<Producto> inactivos = productoService.findInactivos();
        assertTrue(inactivos.stream().anyMatch(p -> p.getProductId().equals(producto.getProductId())));
    }

    @Test
    void testCountByCategoria() {
        List<Object[]> counts = productoService.countByCategoria();
        assertFalse(counts.isEmpty());
        boolean found = counts.stream().anyMatch(arr -> ((Long)arr[0]).equals(categoria.getCategoriaId()) && ((Long)arr[1]) >= 1);
        assertTrue(found);
    }
} 