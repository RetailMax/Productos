package com.duoc.productos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.duoc.productos.service.CategoriaService;
import com.duoc.productos.repository.CategoriaRepository;
import com.duoc.productos.model.Categoria;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

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
		Mockito.when(mockRepo.findById(1L)).thenReturn(java.util.Optional.of(categoria));

		CategoriaService service = new CategoriaService(mockRepo);

		assertTrue(service.getCategoriaById(1L).isPresent());
	}

}
