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
} 