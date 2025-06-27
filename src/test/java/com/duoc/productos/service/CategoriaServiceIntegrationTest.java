package com.duoc.productos.service;

import com.duoc.productos.model.Categoria;
import com.duoc.productos.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import net.datafaker.Faker;

@SpringBootTest
@EntityScan(basePackages = "com.duoc.productos.model")
@EnableJpaRepositories(basePackages = "com.duoc.productos.repository")
@ActiveProfiles("test")
@Transactional
class CategoriaServiceIntegrationTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoriaRepository.deleteAll();
        Faker faker = new Faker();
        // Insertar 10 categorías aleatorias
        for (int i = 0; i < 10; i++) {
            Categoria cat = new Categoria();
            cat.setName(faker.commerce().department() + " " + faker.number().randomDigit());
            cat.setDescription(faker.lorem().sentence());
            cat.setIsActive(faker.bool().bool());
            categoriaRepository.save(cat);
        }
        // Insertar una categoría conocida para los tests específicos
        categoria = new Categoria();
        categoria.setName("Tecnología");
        categoria.setDescription("Productos tecnológicos");
        categoria.setIsActive(true);
        categoria = categoriaRepository.save(categoria);
    }

    @Test
    void testGetAllCategorias() {
        int before = categoriaRepository.findAll().size();
        Categoria nueva = new Categoria();
        nueva.setName("Nueva");
        nueva.setDescription("Desc");
        nueva.setIsActive(true);
        categoriaService.createCategoria(nueva);
        int after = categoriaService.getAllCategorias().size();
        assertTrue(after >= before + 1);
    }

    @Test
    void testGetCategoriaByIdFound() {
        Optional<Categoria> result = categoriaService.getCategoriaById(categoria.getCategoriaId());
        assertTrue(result.isPresent());
        assertEquals("Tecnología", result.get().getName());
    }

    @Test
    void testGetCategoriaByIdNotFound() {
        Optional<Categoria> result = categoriaService.getCategoriaById(999L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateCategoria() {
        Categoria nueva = new Categoria();
        nueva.setName("Nueva");
        nueva.setDescription("Desc");
        nueva.setIsActive(true);
        Categoria result = categoriaService.createCategoria(nueva);
        assertNotNull(result.getCategoriaId());
        assertEquals("Nueva", result.getName());
    }

    @Test
    void testUpdateCategoriaSuccess() {
        Categoria detalles = new Categoria();
        detalles.setName("Actualizada");
        detalles.setDescription("Desc actualizada");
        detalles.setIsActive(false);
        Categoria result = categoriaService.updateCategoria(categoria.getCategoriaId(), detalles);
        assertEquals("Actualizada", result.getName());
        assertEquals("Desc actualizada", result.getDescription());
        assertFalse(result.getIsActive());
    }

    @Test
    void testUpdateCategoriaNotFoundThrows() {
        Categoria detalles = new Categoria();
        Exception ex = assertThrows(RuntimeException.class, () -> categoriaService.updateCategoria(999L, detalles));
        assertEquals("Categoría no encontrada", ex.getMessage());
    }

    @Test
    void testDeleteCategoriaSuccess() {
        categoriaService.deleteCategoria(categoria.getCategoriaId());
        assertFalse(categoriaRepository.findById(categoria.getCategoriaId()).isPresent());
    }

    @Test
    void testDeleteCategoriaNotFoundThrows() {
        Exception ex = assertThrows(RuntimeException.class, () -> categoriaService.deleteCategoria(999L));
        assertEquals("Categoría no encontrada", ex.getMessage());
    }

    @Test
    void testSearchCategorias() {
        Categoria cat = new Categoria();
        cat.setName("Tecnología");
        cat.setDescription("Productos tecnológicos");
        cat.setIsActive(true);
        categoriaService.createCategoria(cat);
        var result = categoriaService.searchCategorias("tec");
        assertTrue(result.size() >= 1);
    }

    @Test
    void testSearchCategoriasEmpty() {
        var result = categoriaService.searchCategorias("nada-que-no-exista");
        assertTrue(result.isEmpty());
    }

    @Test
    void testExistsByNameTrue() {
        assertTrue(categoriaService.existsByName("Tecnología"));
    }

    @Test
    void testExistsByNameFalse() {
        assertFalse(categoriaService.existsByName("NoExiste"));
    }
} 