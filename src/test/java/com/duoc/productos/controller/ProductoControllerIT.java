package com.duoc.productos.controller;

import com.duoc.productos.model.Categoria;
import com.duoc.productos.model.Producto;
import com.duoc.productos.repository.CategoriaRepository;
import com.duoc.productos.repository.ProductoRepository;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductoControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;

    Categoria categoria;
    Producto producto;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setName("CatIT");
        categoria.setDescription("DescIT");
        categoria.setIsActive(true);
        categoria = categoriaRepository.save(categoria);

        producto = new Producto();
        producto.setName("ProdIT");
        producto.setDescription("DescIT");
        producto.setBrand("MarcaIT");
        producto.setBasePrice(100);
        producto.setIsActive(true);
        producto.setCategoria(categoria);
        producto = productoRepository.save(producto);
    }

    @Test
    void testGetProductos() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productoList[0].name").exists());
    }

    @Test
    void testGetProductoById() throws Exception {
        mockMvc.perform(get("/api/products/" + producto.getProductId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ProdIT"));
    }

    @Test
    void testGetProductoByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/products/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProducto() throws Exception {
        Producto nuevo = new Producto();
        nuevo.setName("Nuevo");
        nuevo.setDescription("DescNuevo");
        nuevo.setBrand("MarcaNuevo");
        nuevo.setBasePrice(200);
        nuevo.setIsActive(true);
        nuevo.setCategoria(categoria);
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nuevo"));
    }

    @Test
    void testCreateProductos() throws Exception {
        Producto nuevo = new Producto();
        nuevo.setName("Batch");
        nuevo.setDescription("DescBatch");
        nuevo.setBrand("MarcaBatch");
        nuevo.setBasePrice(300);
        nuevo.setIsActive(true);
        nuevo.setCategoria(categoria);
        mockMvc.perform(post("/api/products/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of(nuevo))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].name").value("Batch"));
    }

    @Test
    void testUpdateProducto() throws Exception {
        producto.setDescription("Actualizado");
        mockMvc.perform(put("/api/products/" + producto.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Actualizado"));
    }

    @Test
    void testDeleteProducto() throws Exception {
        mockMvc.perform(delete("/api/products/" + producto.getProductId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActivateProducto() throws Exception {
        producto.setIsActive(false);
        productoRepository.save(producto);
        mockMvc.perform(put("/api/products/" + producto.getProductId() + "/activate"))
                .andExpect(status().isOk());
        Producto actualizado = productoRepository.findById(producto.getProductId()).get();
        assertTrue(actualizado.getIsActive());
    }

    @Test
    void testSearchProductos() throws Exception {
        mockMvc.perform(get("/api/products/search").param("q", "ProdIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ProdIT"));
    }

    @Test
    void testExistsByName() throws Exception {
        mockMvc.perform(get("/api/products/exists").param("name", "ProdIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));
        mockMvc.perform(get("/api/products/exists").param("name", "NoExiste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false));
    }

    @Test
    void testGetProductosInactivos() throws Exception {
        producto.setIsActive(false);
        productoRepository.save(producto);
        mockMvc.perform(get("/api/products/inactivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isActive").value(false));
    }

    @Test
    void testCountByCategoria() throws Exception {
        mockMvc.perform(get("/api/products/count-by-categoria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*][0]").value(org.hamcrest.Matchers.hasItem(categoria.getCategoriaId().intValue())));
    }

    @Test
    void testGetProductosWithPagination() throws Exception {
        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductosWithCategoriaFilter() throws Exception {
        mockMvc.perform(get("/api/products")
                .param("categoria", categoria.getCategoriaId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateProductoNotFound() throws Exception {
        Producto nuevo = new Producto();
        nuevo.setName("NoExiste");
        nuevo.setDescription("Desc");
        nuevo.setBrand("Marca");
        nuevo.setBasePrice(100);
        nuevo.setIsActive(true);
        nuevo.setCategoria(categoria);
        mockMvc.perform(put("/api/products/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProductoNotFound() throws Exception {
        mockMvc.perform(delete("/api/products/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProductoInvalid() throws Exception {
        Producto invalido = new Producto();
        // Falta nombre, descripción, etc.
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSearchProductosNoResults() throws Exception {
        mockMvc.perform(get("/api/products/search").param("q", "NoExiste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetProductosPaginationAndSort() throws Exception {
        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "name,desc"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductosCategoriaAndPagination() throws Exception {
        mockMvc.perform(get("/api/products")
                .param("categoria", categoria.getCategoriaId().toString())
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductosOnlyCategoria() throws Exception {
        mockMvc.perform(get("/api/products")
                .param("categoria", categoria.getCategoriaId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateProductosEmptyList() throws Exception {
        mockMvc.perform(post("/api/products/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testCreateProductosInvalid() throws Exception {
        Producto invalido = new Producto();
        mockMvc.perform(post("/api/products/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of(invalido))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProductosMultiple() throws Exception {
        Producto p1 = new Producto();
        p1.setName("Multi1");
        p1.setDescription("Desc1");
        p1.setBrand("Marca1");
        p1.setBasePrice(10);
        p1.setIsActive(true);
        p1.setCategoria(categoria);
        Producto p2 = new Producto();
        p2.setName("Multi2");
        p2.setDescription("Desc2");
        p2.setBrand("Marca2");
        p2.setBasePrice(20);
        p2.setIsActive(true);
        p2.setCategoria(categoria);
        mockMvc.perform(post("/api/products/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of(p1, p2))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testActivateProductoNotFound() throws Exception {
        mockMvc.perform(put("/api/products/999999/activate"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProductosBatchConProductoInvalido() throws Exception {
        Producto valido = new Producto();
        valido.setName("Valido");
        valido.setDescription("Desc");
        valido.setBrand("Marca");
        valido.setBasePrice(10);
        valido.setIsActive(true);
        valido.setCategoria(categoria);
        Producto invalido = new Producto(); // Sin nombre ni descripción
        mockMvc.perform(post("/api/products/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of(valido, invalido))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProductosBatchExactamente100() throws Exception {
        List<Producto> productos = new java.util.ArrayList<>();
        for (int i = 1; i <= 100; i++) { // Exactamente 100 productos, debería funcionar
            Producto p = new Producto();
            p.setName("Prod" + i);
            p.setDescription("Desc" + i);
            p.setBrand("Marca" + i);
            p.setBasePrice(10 * i);
            p.setIsActive(true);
            p.setCategoria(categoria);
            productos.add(p);
        }
        mockMvc.perform(post("/api/products/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productos)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(100));
    }

    @Test
    void testCreateProductosBatchMasDe100() throws Exception {
        List<Producto> productos = new java.util.ArrayList<>();
        for (int i = 1; i <= 101; i++) { // 101 productos, debería fallar
            Producto p = new Producto();
            p.setName("Prod" + i);
            p.setDescription("Desc" + i);
            p.setBrand("Marca" + i);
            p.setBasePrice(10 * i);
            p.setIsActive(true);
            p.setCategoria(categoria);
            productos.add(p);
        }
        mockMvc.perform(post("/api/products/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productos)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No se pueden agregar más de 100 productos por batch."));
    }
} 