package com.duoc.productos.controller;

import com.duoc.productos.model.Categoria;
import com.duoc.productos.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setCategoriaId(1L);
        categoria.setName("Tecnología");
        categoria.setDescription("Productos tecnológicos");
        categoria.setIsActive(true);
        categoria.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAllCategorias() throws Exception {
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaService.getAllCategorias()).thenReturn(categorias);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoriaId").value(1))
                .andExpect(jsonPath("$[0].name").value("Tecnología"));
    }

    @Test
    void testGetCategoriaById() throws Exception {
        when(categoriaService.getCategoriaById(1L)).thenReturn(Optional.of(categoria));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoriaId").value(1))
                .andExpect(jsonPath("$.name").value("Tecnología"));
    }

    @Test
    void testGetCategoriaByIdNotFound() throws Exception {
        when(categoriaService.getCategoriaById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/categorias/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCategoria() throws Exception {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setName("Nueva Categoría");
        nuevaCategoria.setDescription("Descripción nueva");

        when(categoriaService.createCategoria(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaCategoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoriaId").value(1))
                .andExpect(jsonPath("$.name").value("Tecnología"));
    }

    @Test
    void testSearchCategorias() throws Exception {
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaService.searchCategorias(anyString())).thenReturn(categorias);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/categorias/search")
                .param("q", "tecnología"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoriaId").value(1))
                .andExpect(jsonPath("$[0].name").value("Tecnología"));
    }

    @Test
    void testExistsByName() throws Exception {
        when(categoriaService.existsByName("Tecnología")).thenReturn(true);
        when(categoriaService.existsByName("NoExiste")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/categorias/exists")
                .param("name", "Tecnología"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/categorias/exists")
                .param("name", "NoExiste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false));
    }
} 