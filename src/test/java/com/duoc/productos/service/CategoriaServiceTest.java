package com.duoc.productos.service;

import com.duoc.productos.model.Categoria;
import com.duoc.productos.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import org.junit.jupiter.api.Tag;

@Tag("unit")
class CategoriaServiceTest {
    private CategoriaRepository categoriaRepository;
    private CategoriaService categoriaService;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoriaRepository = Mockito.mock(CategoriaRepository.class);
        categoriaService = new CategoriaService(categoriaRepository);
        categoria = new Categoria();
        categoria.setCategoriaId(1L);
        categoria.setName("Tecnología");
        categoria.setDescription("Productos tecnológicos");
        categoria.setIsActive(true);
    }

    @Test
    void testGetAllCategorias() {
        List<Categoria> categorias = Arrays.asList(categoria);
        Mockito.when(categoriaRepository.findAll()).thenReturn(categorias);
        List<Categoria> result = categoriaService.getAllCategorias();
        assertEquals(1, result.size());
        assertEquals("Tecnología", result.get(0).getName());
    }

    @Test
    void testGetAllCategoriasEmpty() {
        Mockito.when(categoriaRepository.findAll()).thenReturn(Collections.emptyList());
        List<Categoria> result = categoriaService.getAllCategorias();
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCategoriaByIdFound() {
        Mockito.when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        Optional<Categoria> result = categoriaService.getCategoriaById(1L);
        assertTrue(result.isPresent());
        assertEquals("Tecnología", result.get().getName());
    }

    @Test
    void testGetCategoriaByIdNotFound() {
        Mockito.when(categoriaRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Categoria> result = categoriaService.getCategoriaById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateCategoria() {
        Mockito.when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        Categoria nueva = new Categoria();
        nueva.setName("Nueva");
        nueva.setDescription("Desc");
        Categoria result = categoriaService.createCategoria(nueva);
        assertEquals("Tecnología", result.getName());
    }

    @Test
    void testUpdateCategoriaSuccess() {
        Categoria detalles = new Categoria();
        detalles.setName("Actualizada");
        detalles.setDescription("Desc actualizada");
        detalles.setIsActive(false);
        Mockito.when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        Mockito.when(categoriaRepository.save(any(Categoria.class))).thenReturn(detalles);
        Categoria result = categoriaService.updateCategoria(1L, detalles);
        assertEquals("Actualizada", result.getName());
        assertEquals("Desc actualizada", result.getDescription());
        assertFalse(result.getIsActive());
    }

    @Test
    void testUpdateCategoriaNotFoundThrows() {
        Categoria detalles = new Categoria();
        Mockito.when(categoriaRepository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> categoriaService.updateCategoria(2L, detalles));
        assertEquals("Categoría no encontrada", ex.getMessage());
    }

    @Test
    void testDeleteCategoriaSuccess() {
        Mockito.when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        categoriaService.deleteCategoria(1L);
        Mockito.verify(categoriaRepository).delete(categoria);
    }

    @Test
    void testDeleteCategoriaNotFoundThrows() {
        Mockito.when(categoriaRepository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> categoriaService.deleteCategoria(2L));
        assertEquals("Categoría no encontrada", ex.getMessage());
    }

    @Test
    void testSearchCategorias() {
        List<Categoria> categorias = Arrays.asList(categoria);
        Mockito.when(categoriaRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(anyString(), anyString())).thenReturn(categorias);
        List<Categoria> result = categoriaService.searchCategorias("tec");
        assertEquals(1, result.size());
    }

    @Test
    void testSearchCategoriasEmpty() {
        Mockito.when(categoriaRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(anyString(), anyString())).thenReturn(Collections.emptyList());
        List<Categoria> result = categoriaService.searchCategorias("nada");
        assertTrue(result.isEmpty());
    }

    @Test
    void testExistsByNameTrue() {
        Mockito.when(categoriaRepository.existsByNameIgnoreCase("Tecnología")).thenReturn(true);
        assertTrue(categoriaService.existsByName("Tecnología"));
    }

    @Test
    void testExistsByNameFalse() {
        Mockito.when(categoriaRepository.existsByNameIgnoreCase("NoExiste")).thenReturn(false);
        assertFalse(categoriaService.existsByName("NoExiste"));
    }
} 