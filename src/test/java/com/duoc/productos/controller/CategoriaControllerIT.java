package com.duoc.productos.controller;

import com.duoc.productos.model.Categoria;
import com.duoc.productos.repository.CategoriaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoriaControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setName("CatIT");
        categoria.setDescription("DescIT");
        categoria.setIsActive(true);
        categoria.setCreatedAt(LocalDateTime.now());
        categoria = categoriaRepository.save(categoria);
    }

    @Test
    void testGetAllCategorias() throws Exception {
        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void testGetCategoriaById() throws Exception {
        mockMvc.perform(get("/categorias/" + categoria.getCategoriaId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("CatIT"));
    }

    @Test
    void testGetCategoriaByIdNotFound() throws Exception {
        mockMvc.perform(get("/categorias/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCategoria() throws Exception {
        Categoria nueva = new Categoria();
        nueva.setName("Nueva");
        nueva.setDescription("DescNueva");
        nueva.setIsActive(true);
        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nueva"));
    }

    @Test
    void testUpdateCategoria() throws Exception {
        categoria.setDescription("Actualizada");
        mockMvc.perform(put("/categorias/" + categoria.getCategoriaId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Actualizada"));
    }

    @Test
    void testUpdateCategoriaNotFound() throws Exception {
        Categoria nueva = new Categoria();
        nueva.setName("NoExiste");
        nueva.setDescription("Desc");
        nueva.setIsActive(true);
        mockMvc.perform(put("/categorias/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCategoria() throws Exception {
        mockMvc.perform(delete("/categorias/" + categoria.getCategoriaId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCategoriaNotFound() throws Exception {
        mockMvc.perform(delete("/categorias/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchCategorias() throws Exception {
        mockMvc.perform(get("/categorias/search").param("q", "CatIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("CatIT"));
    }

    @Test
    void testSearchCategoriasNoResults() throws Exception {
        mockMvc.perform(get("/categorias/search").param("q", "NoExiste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testExistsByName() throws Exception {
        mockMvc.perform(get("/categorias/exists").param("name", "CatIT"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        mockMvc.perform(get("/categorias/exists").param("name", "NoExiste"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
} 