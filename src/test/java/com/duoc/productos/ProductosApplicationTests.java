package com.duoc.productos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.duoc.productos.service.CategoriaService;
import com.duoc.productos.repository.CategoriaRepository;
import com.duoc.productos.model.Categoria;
import com.duoc.productos.controller.CategoriaController;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@SpringBootTest
class ProductosApplicationTests {

	@Test
	void testExistsByName() {
		//Mock del repositorio	
		CategoriaRepository mockRepo = Mockito.mock(CategoriaRepository.class);
		Mockito.when(mockRepo.existsByNameIgnoreCase("Tecnología")).thenReturn(true);
		Mockito.when(mockRepo.existsByNameIgnoreCase("NoExiste")).thenReturn(false);

		// Usa el constructor para inyectar el mock
		CategoriaService service = new CategoriaService(mockRepo);

		// Pruebas
		assertTrue(service.existsByName("Tecnología"));
		assertFalse(service.existsByName("NoExiste"));
	}

	@Test
	void testGetAllCategorias() {
		CategoriaRepository mockRepo = Mockito.mock(CategoriaRepository.class);
		List<Categoria> categorias = List.of(
			new Categoria(), new Categoria()
		);
		Mockito.when(mockRepo.findAll()).thenReturn(categorias);

		CategoriaService service = new CategoriaService(mockRepo);

		assertEquals(2, service.getAllCategorias().size());
	}

	@Test
	void testGetCategoriaById() {
		CategoriaRepository mockRepo = Mockito.mock(CategoriaRepository.class);
		Categoria categoria = new Categoria();
		Mockito.when(mockRepo.findById(1L)).thenReturn(Optional.of(categoria));

		CategoriaService service = new CategoriaService(mockRepo);

		assertTrue(service.getCategoriaById(1L).isPresent());
	}

	@Test
	void testGetCategoriaByIdNotFound() {
		CategoriaRepository mockRepo = Mockito.mock(CategoriaRepository.class);
		Mockito.when(mockRepo.findById(999L)).thenReturn(Optional.empty());

		CategoriaService service = new CategoriaService(mockRepo);

		assertFalse(service.getCategoriaById(999L).isPresent());
	}

	@Test
	void testCreateCategoria() {
		CategoriaRepository mockRepo = Mockito.mock(CategoriaRepository.class);
		Categoria categoria = new Categoria();
		categoria.setName("Nueva Categoría");
		categoria.setDescription("Descripción de prueba");
		
		Mockito.when(mockRepo.save(Mockito.any(Categoria.class))).thenReturn(categoria);

		CategoriaService service = new CategoriaService(mockRepo);

		Categoria result = service.createCategoria(categoria);
		assertNotNull(result);
		assertEquals("Nueva Categoría", result.getName());
	}

	@Test
	void testUpdateCategoria() {
		CategoriaRepository mockRepo = Mockito.mock(CategoriaRepository.class);
		Categoria categoriaExistente = new Categoria();
		categoriaExistente.setCategoriaId(1L);
		categoriaExistente.setName("Categoría Original");
		
		Categoria categoriaActualizada = new Categoria();
		categoriaActualizada.setName("Categoría Actualizada");
		categoriaActualizada.setDescription("Nueva descripción");
		
		Mockito.when(mockRepo.findById(1L)).thenReturn(Optional.of(categoriaExistente));
		Mockito.when(mockRepo.save(Mockito.any(Categoria.class))).thenReturn(categoriaActualizada);

		CategoriaService service = new CategoriaService(mockRepo);

		Categoria result = service.updateCategoria(1L, categoriaActualizada);
		assertNotNull(result);
		assertEquals("Categoría Actualizada", result.getName());
	}

	@Test
	void testUpdateCategoriaNotFound() {
		CategoriaRepository mockRepo = Mockito.mock(CategoriaRepository.class);
		Mockito.when(mockRepo.findById(999L)).thenReturn(Optional.empty());

		CategoriaService service = new CategoriaService(mockRepo);
		Categoria categoriaActualizada = new Categoria();

		assertThrows(RuntimeException.class, () -> {
			service.updateCategoria(999L, categoriaActualizada);
		});
	}

	@Test
	void testDeleteCategoria() {
		CategoriaRepository mockRepo = Mockito.mock(CategoriaRepository.class);
		Categoria categoria = new Categoria();
		categoria.setCategoriaId(1L);
		
		Mockito.when(mockRepo.findById(1L)).thenReturn(Optional.of(categoria));
		Mockito.doNothing().when(mockRepo).delete(categoria);

		CategoriaService service = new CategoriaService(mockRepo);

		assertDoesNotThrow(() -> {
			service.deleteCategoria(1L);
		});
	}

	@Test
	void testDeleteCategoriaNotFound() {
		CategoriaRepository mockRepo = Mockito.mock(CategoriaRepository.class);
		Mockito.when(mockRepo.findById(999L)).thenReturn(Optional.empty());

		CategoriaService service = new CategoriaService(mockRepo);

		assertThrows(RuntimeException.class, () -> {
			service.deleteCategoria(999L);
		});
	}

	@Test
	void testSearchCategorias() {
		CategoriaRepository mockRepo = Mockito.mock(CategoriaRepository.class);
		List<Categoria> categorias = List.of(
			new Categoria(), new Categoria()
		);
		Mockito.when(mockRepo.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("test", "test"))
			.thenReturn(categorias);

		CategoriaService service = new CategoriaService(mockRepo);

		List<Categoria> result = service.searchCategorias("test");
		assertEquals(2, result.size());
	}

	@Test
	void testSearchCategoriasEmpty() {
		CategoriaRepository mockRepo = Mockito.mock(CategoriaRepository.class);
		Mockito.when(mockRepo.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("noexiste", "noexiste"))
			.thenReturn(List.of());

		CategoriaService service = new CategoriaService(mockRepo);

		List<Categoria> result = service.searchCategorias("noexiste");
		assertTrue(result.isEmpty());
	}

	@Test
	void testCategoriaModel() {
		Categoria categoria = new Categoria();
		categoria.setCategoriaId(1L);
		categoria.setName("Test Categoría");
		categoria.setDescription("Test Descripción");
		categoria.setIsActive(true);
		categoria.setCreatedAt(LocalDateTime.now());

		assertEquals(1L, categoria.getCategoriaId());
		assertEquals("Test Categoría", categoria.getName());
		assertEquals("Test Descripción", categoria.getDescription());
		assertTrue(categoria.getIsActive());
		assertNotNull(categoria.getCreatedAt());
	}

	@Test
	void mainRuns() {
		ProductosApplication.main(new String[] {});
	}

}
