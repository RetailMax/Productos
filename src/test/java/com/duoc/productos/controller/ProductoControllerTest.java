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

    @Test
    void testGetProductos_paginadoYOrdenado() throws Exception {
        Mockito.when(productoService.findAllActivePaged(Mockito.any())).thenReturn(new org.springframework.data.domain.PageImpl<>(List.of()));
        mockMvc.perform(get("/api/productos?page=0&size=10&sort=name,desc"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductos_filtradoPorCategoria() throws Exception {
        Mockito.when(productoService.findByCategoria(Mockito.eq(1L))).thenReturn(List.of());
        mockMvc.perform(get("/api/productos?categoria=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductoById_notFound_ById999() throws Exception {
        Mockito.when(productoService.findById(999L)).thenReturn(java.util.Optional.empty());
        mockMvc.perform(get("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProducto_excepcion() throws Exception {
        Producto producto = new Producto();
        producto.setName("Excepcion");
        Mockito.when(productoService.existsById(1L)).thenReturn(true);
        Mockito.when(productoService.save(Mockito.any(Producto.class))).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(producto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProducto_excepcion() throws Exception {
        Mockito.when(productoService.existsById(1L)).thenReturn(true);
        Mockito.doThrow(new RuntimeException("Error")).when(productoService).deleteById(1L);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/productos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActivarProducto_notFound_ActivarId999() throws Exception {
        Mockito.when(productoService.activarProducto(999L)).thenReturn(false);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/productos/999/activar"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProducto_datosLimite() throws Exception {
        String nombre = "N".repeat(100);
        String descripcion = "D".repeat(500);
        Producto producto = new Producto();
        producto.setName(nombre);
        producto.setDescription(descripcion);
        producto.setBrand("MarcaLarga");
        producto.setBasePrice(0);
        producto.setIsActive(true);

        Mockito.when(productoService.save(Mockito.any(Producto.class))).thenReturn(producto);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(nombre))
                .andExpect(jsonPath("$.description").value(descripcion))
                .andExpect(jsonPath("$.basePrice").value(0));
    }

    @Test
    void testCreateProducto_isActiveNull() throws Exception {
        Producto producto = new Producto();
        producto.setName("ProdActivo");
        producto.setDescription("DescActivo");
        producto.setBrand("MarcaActiva");
        producto.setBasePrice(100);
        producto.setIsActive(null); // Debería quedar true por defecto

        Producto productoGuardado = new Producto();
        productoGuardado.setName("ProdActivo");
        productoGuardado.setDescription("DescActivo");
        productoGuardado.setBrand("MarcaActiva");
        productoGuardado.setBasePrice(100);
        productoGuardado.setIsActive(true);

        Mockito.when(productoService.save(Mockito.any(Producto.class))).thenReturn(productoGuardado);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void testSearchProductos() throws Exception {
        Producto producto = new Producto();
        producto.setProductId(1L);
        producto.setName("BuscarMe");
        producto.setDescription("Desc");
        producto.setBrand("Marca");
        producto.setBasePrice(100);
        producto.setIsActive(true);

        Mockito.when(productoService.searchProductos("BuscarMe")).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos/search?q=BuscarMe").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("BuscarMe"));
    }

    @Test
    void testGetProductosInactivos() throws Exception {
        Producto producto = new Producto();
        producto.setProductId(2L);
        producto.setName("Inactivo");
        producto.setDescription("DescInac");
        producto.setBrand("MarcaInac");
        producto.setBasePrice(50);
        producto.setIsActive(false);

        Mockito.when(productoService.findInactivos()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos/inactivos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isActive").value(false));
    }

    @Test
    void testCountByCategoria() throws Exception {
        java.util.List<Object[]> lista = new java.util.ArrayList<>();
        lista.add(new Object[]{1L, 5L}); // Ejemplo: categoriaId=1, cantidad=5
        Mockito.when(productoService.countByCategoria()).thenReturn(lista);

        mockMvc.perform(get("/api/productos/count-by-categoria").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0][0]").value(1))
                .andExpect(jsonPath("$[0][1]").value(5));
    }

    @Test
    void testExistsByName_true() throws Exception {
        Mockito.when(productoService.existsByName("Existente")).thenReturn(true);
        mockMvc.perform(get("/api/productos/exists?name=Existente").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));
    }

    @Test
    void testExistsByName_false() throws Exception {
        Mockito.when(productoService.existsByName("NoExiste")).thenReturn(false);
        mockMvc.perform(get("/api/productos/exists?name=NoExiste").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false));
    }

    @Test
    void testUpdateProducto_notFound_idInexistente() throws Exception {
        Producto producto = new Producto();
        producto.setName("NoExiste");
        producto.setDescription("Desc");
        producto.setBrand("Marca");
        producto.setBasePrice(100);
        producto.setIsActive(true);
        Mockito.when(productoService.existsById(999L)).thenReturn(false);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/productos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(producto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProducto_notFound_idInexistente() throws Exception {
        Mockito.when(productoService.existsById(999L)).thenReturn(false);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActivarProducto_notFound_idInexistente() throws Exception {
        Mockito.when(productoService.activarProducto(999L)).thenReturn(false);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/productos/999/activar"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchProductos_sinResultados() throws Exception {
        Mockito.when(productoService.searchProductos("Nada")).thenReturn(java.util.Collections.emptyList());
        mockMvc.perform(get("/api/productos/search?q=Nada").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetProductosInactivos_vacio() throws Exception {
        Mockito.when(productoService.findInactivos()).thenReturn(java.util.Collections.emptyList());
        mockMvc.perform(get("/api/productos/inactivos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testCountByCategoria_vacio() throws Exception {
        Mockito.when(productoService.countByCategoria()).thenReturn(java.util.Collections.emptyList());
        mockMvc.perform(get("/api/productos/count-by-categoria").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testExistsByName_noExiste() throws Exception {
        Mockito.when(productoService.existsByName("NoExiste")).thenReturn(false);
        mockMvc.perform(get("/api/productos/exists?name=NoExiste").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false));
    }

    @Test
    void testGetProductos_combinacionesFiltros() throws Exception {
        // Solo categoría
        Mockito.when(productoService.findByCategoria(1L)).thenReturn(java.util.Collections.emptyList());
        mockMvc.perform(get("/api/productos?categoria=1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // Solo paginación
        Mockito.when(productoService.findAllActivePaged(Mockito.any())).thenReturn(new org.springframework.data.domain.PageImpl<>(java.util.Collections.emptyList()));
        mockMvc.perform(get("/api/productos?page=0&size=10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // Categoría y paginación
        Mockito.when(productoService.findByCategoriaPaged(Mockito.eq(1L), Mockito.any())).thenReturn(new org.springframework.data.domain.PageImpl<>(java.util.Collections.emptyList()));
        mockMvc.perform(get("/api/productos?categoria=1&page=0&size=10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // Sin filtros
        Mockito.when(productoService.findAll()).thenReturn(java.util.Collections.emptyList());
        mockMvc.perform(get("/api/productos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
} 