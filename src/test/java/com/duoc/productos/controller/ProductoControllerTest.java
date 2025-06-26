package com.duoc.productos.controller;

import com.duoc.productos.model.Producto;
import com.duoc.productos.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductoService productoService;
    @Autowired
    private ObjectMapper objectMapper;
    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setProductId(1L);
        producto.setName("Test");
        producto.setDescription("Desc");
        producto.setBrand("Marca");
        producto.setBasePrice(100);
        producto.setIsActive(true);
    }

    @Test
    void testGetProductoByIdNotFound() throws Exception {
        when(productoService.findById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProductoNotFound() throws Exception {
        when(productoService.existsById(anyLong())).thenReturn(false);
        mockMvc.perform(put("/api/productos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProductoNotFound() throws Exception {
        when(productoService.existsById(anyLong())).thenReturn(false);
        mockMvc.perform(delete("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActivarProductoNotFound() throws Exception {
        when(productoService.activarProducto(anyLong())).thenReturn(false);
        mockMvc.perform(put("/api/productos/999/activar"))
                .andExpect(status().isNotFound());
    }
} 