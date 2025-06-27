package com.duoc.productos.controller;

import com.duoc.productos.model.Producto;
import com.duoc.productos.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Test
    void testGetProductos() throws Exception {
        Producto producto = new Producto();
        producto.setProductId(1L);
        producto.setName("TestProd");
        producto.setDescription("Desc");
        producto.setBrand("Marca");
        producto.setBasePrice(100);
        producto.setIsActive(true);

        Mockito.when(productoService.findAll()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productoList[0].name").value("TestProd"));
    }

    @Test
    void testGetProductoById_success() throws Exception {
        Producto producto = new Producto();
        producto.setProductId(1L);
        producto.setName("TestProd");
        producto.setDescription("Desc");
        producto.setBrand("Marca");
        producto.setBasePrice(100);
        producto.setIsActive(true);

        Mockito.when(productoService.findById(1L)).thenReturn(java.util.Optional.of(producto));

        mockMvc.perform(get("/api/productos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestProd"));
    }

    @Test
    void testGetProductoById_notFound() throws Exception {
        Mockito.when(productoService.findById(99L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/productos/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProducto_success() throws Exception {
        Producto producto = new Producto();
        producto.setName("Nuevo");
        producto.setDescription("DescNuevo");
        producto.setBrand("MarcaNuevo");
        producto.setBasePrice(200);
        producto.setIsActive(true);

        Mockito.when(productoService.save(Mockito.any(Producto.class))).thenReturn(producto);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nuevo"));
    }

    @Test
    void testUpdateProducto_success() throws Exception {
        Producto producto = new Producto();
        producto.setProductId(1L);
        producto.setName("Actualizado");
        producto.setDescription("DescAct");
        producto.setBrand("MarcaAct");
        producto.setBasePrice(300);
        producto.setIsActive(true);

        Mockito.when(productoService.existsById(1L)).thenReturn(true);
        Mockito.when(productoService.save(Mockito.any(Producto.class))).thenReturn(producto);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Actualizado"));
    }

    @Test
    void testUpdateProducto_notFound() throws Exception {
        Producto producto = new Producto();
        producto.setName("NoExiste");
        producto.setDescription("Desc");
        producto.setBrand("Marca");
        producto.setBasePrice(100);
        producto.setIsActive(true);

        Mockito.when(productoService.existsById(99L)).thenReturn(false);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/productos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(producto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProducto_success() throws Exception {
        Mockito.when(productoService.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(productoService).deleteById(1L);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProducto_notFound() throws Exception {
        Mockito.when(productoService.existsById(99L)).thenReturn(false);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActivarProducto_success() throws Exception {
        Mockito.when(productoService.activarProducto(1L)).thenReturn(true);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/productos/1/activar"))
                .andExpect(status().isOk());
    }

    @Test
    void testActivarProducto_notFound() throws Exception {
        Mockito.when(productoService.activarProducto(99L)).thenReturn(false);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/productos/99/activar"))
                .andExpect(status().isNotFound());
    }
} 