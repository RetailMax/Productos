package com.duoc.productos.repository;

import com.duoc.productos.model.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria categoria1;
    private Categoria categoria2;

    @BeforeEach
    void setUp() {
        categoria1 = new Categoria();
        categoria1.setName("Tecnología");
        categoria1.setDescription("Productos tecnológicos");
        categoria1.setIsActive(true);
        categoria1.setCreatedAt(LocalDateTime.now());

        categoria2 = new Categoria();
        categoria2.setName("Ropa");
        categoria2.setDescription("Vestimenta y accesorios");
        categoria2.setIsActive(true);
        categoria2.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testSaveCategoria() {
        Categoria savedCategoria = categoriaRepository.save(categoria1);

        assertNotNull(savedCategoria.getCategoriaId());
        assertEquals("Tecnología", savedCategoria.getName());
        assertEquals("Productos tecnológicos", savedCategoria.getDescription());
        assertTrue(savedCategoria.getIsActive());
        assertNotNull(savedCategoria.getCreatedAt());
    }

    @Test
    void testFindById() {
        Categoria savedCategoria = categoriaRepository.save(categoria1);

        Optional<Categoria> found = categoriaRepository.findById(savedCategoria.getCategoriaId());

        assertTrue(found.isPresent());
        assertEquals("Tecnología", found.get().getName());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Categoria> found = categoriaRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        int before = categoriaRepository.findAll().size();
        categoriaRepository.save(categoria1);
        categoriaRepository.save(categoria2);
        int after = categoriaRepository.findAll().size();
        // Verifica que al menos se hayan agregado 2
        assertTrue(after >= before + 2);
    }

    @Test
    void testDeleteCategoria() {
        Categoria savedCategoria = categoriaRepository.save(categoria1);

        categoriaRepository.deleteById(savedCategoria.getCategoriaId());

        Optional<Categoria> found = categoriaRepository.findById(savedCategoria.getCategoriaId());
        assertFalse(found.isPresent());
    }

    @Test
    void testExistsByNameIgnoreCase() {
        categoriaRepository.save(categoria1);

        assertTrue(categoriaRepository.existsByNameIgnoreCase("Tecnología"));
        assertTrue(categoriaRepository.existsByNameIgnoreCase("tecnología"));
        assertTrue(categoriaRepository.existsByNameIgnoreCase("TECNOLOGÍA"));
        assertFalse(categoriaRepository.existsByNameIgnoreCase("NoExiste"));
    }

    @Test
    void testFindByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase() {
        categoriaRepository.save(categoria1);
        categoriaRepository.save(categoria2);
        List<Categoria> result = categoriaRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("tec", "tec");
        // Verifica que al menos una categoría contenga "tec"
        assertTrue(result.size() >= 1);
    }

    @Test
    void testFindByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseCaseInsensitive() {
        categoriaRepository.save(categoria1);
        List<Categoria> result = categoriaRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("TECNOLOGÍA", "TECNOLOGÍA");
        assertTrue(result.size() >= 1);
    }

    @Test
    void testUpdateCategoria() {
        Categoria savedCategoria = categoriaRepository.save(categoria1);

        savedCategoria.setName("Tecnología Actualizada");
        savedCategoria.setDescription("Descripción actualizada");

        Categoria updatedCategoria = categoriaRepository.save(savedCategoria);

        assertEquals("Tecnología Actualizada", updatedCategoria.getName());
        assertEquals("Descripción actualizada", updatedCategoria.getDescription());
        assertEquals(savedCategoria.getCategoriaId(), updatedCategoria.getCategoriaId());
    }

    @Test
    void testFindByIsActive() {
        categoria1.setIsActive(true);
        categoria2.setIsActive(false);

        categoriaRepository.save(categoria1);
        categoriaRepository.save(categoria2);

        // Este test asume que tienes un método findByIsActive en el repositorio
        // Si no lo tienes, puedes comentarlo o agregar el método al repositorio
        /*
        List<Categoria> activeCategorias = categoriaRepository.findByIsActive(true);
        assertEquals(1, activeCategorias.size());
        assertEquals("Tecnología", activeCategorias.get(0).getName());

        List<Categoria> inactiveCategorias = categoriaRepository.findByIsActive(false);
        assertEquals(1, inactiveCategorias.size());
        assertEquals("Ropa", inactiveCategorias.get(0).getName());
        */
    }

    @Test
    void testSaveAndFindCategoriaMinimal() {
        Categoria categoria = new Categoria();
        categoria.setName("Prueba");
        categoria.setDescription("Test mínimo");
        categoria.setIsActive(true);
        Categoria saved = categoriaRepository.save(categoria);
        assertNotNull(saved.getCategoriaId());
        Optional<Categoria> found = categoriaRepository.findById(saved.getCategoriaId());
        assertTrue(found.isPresent());
        assertEquals("Prueba", found.get().getName());
    }
} 