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
import net.datafaker.Faker;

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
        categoriaRepository.deleteAll();
        productoRepository.deleteAll();
        Faker faker = new Faker();
        // Insertar 5 categorías aleatorias
        for (int i = 0; i < 5; i++) {
            Categoria cat = new Categoria();
            cat.setName(faker.commerce().department() + " " + faker.number().randomDigit());
            cat.setDescription(faker.lorem().sentence());
            cat.setIsActive(faker.bool().bool());
            cat = categoriaRepository.save(cat);
            // Insertar 5 productos por categoría
            for (int j = 0; j < 5; j++) {
                Producto prod = new Producto();
                prod.setName(faker.commerce().productName() + " " + faker.number().randomDigit());
                prod.setDescription(faker.lorem().sentence());
                prod.setBrand(faker.company().name());
                prod.setBasePrice(faker.number().numberBetween(10, 1000));
                prod.setIsActive(faker.bool().bool());
                prod.setCategoria(cat);
                productoRepository.save(prod);
            }
        }
        // Insertar una categoría y producto conocidos para los tests específicos
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
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void testGetProductoById() throws Exception {
        mockMvc.perform(get("/api/productos/" + producto.getProductId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ProdIT"));
    }

    @Test
    void testGetProductoByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/productos/999999"))
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
        mockMvc.perform(post("/api/productos")
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
        mockMvc.perform(post("/api/productos/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of(nuevo))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].name").value("Batch"));
    }

    @Test
    void testUpdateProducto() throws Exception {
        producto.setDescription("Actualizado");
        mockMvc.perform(put("/api/productos/" + producto.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Actualizado"));
    }

    @Test
    void testDeleteProducto() throws Exception {
        mockMvc.perform(delete("/api/productos/" + producto.getProductId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActivarProducto() throws Exception {
        producto.setIsActive(false);
        productoRepository.save(producto);
        mockMvc.perform(put("/api/productos/" + producto.getProductId() + "/activar"))
                .andExpect(status().isOk());
        Producto actualizado = productoRepository.findById(producto.getProductId()).get();
        assertTrue(actualizado.getIsActive());
    }

    @Test
    void testSearchProductos() throws Exception {
        mockMvc.perform(get("/api/productos/search").param("q", "ProdIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ProdIT"));
    }

    @Test
    void testExistsByName() throws Exception {
        mockMvc.perform(get("/api/productos/exists").param("name", "ProdIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));
        mockMvc.perform(get("/api/productos/exists").param("name", "NoExiste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false));
    }

    @Test
    void testGetProductosInactivos() throws Exception {
        producto.setIsActive(false);
        productoRepository.save(producto);
        mockMvc.perform(get("/api/productos/inactivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isActive").value(false));
    }

    @Test
    void testCountByCategoria() throws Exception {
        mockMvc.perform(get("/api/productos/count-by-categoria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*][0]").value(org.hamcrest.Matchers.hasItem(categoria.getCategoriaId().intValue())));
    }

    @Test
    void testGetProductosWithPagination() throws Exception {
        mockMvc.perform(get("/api/productos")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductosWithCategoriaFilter() throws Exception {
        mockMvc.perform(get("/api/productos")
                .param("categoria", categoria.getCategoriaId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateProductoNotFound() throws Exception {
        Producto update = new Producto();
        update.setName("Nuevo");
        update.setDescription("Desc");
        update.setBrand("Marca");
        update.setBasePrice(1000);
        update.setIsActive(true);
        mockMvc.perform(put("/api/productos/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProductoNotFound() throws Exception {
        mockMvc.perform(delete("/api/productos/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProductoInvalid() throws Exception {
        Producto invalido = new Producto();
        // Falta nombre, descripción, etc.
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSearchProductosNoResults() throws Exception {
        mockMvc.perform(get("/api/productos/search").param("q", "NoExiste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetProductosPaginationAndSort() throws Exception {
        mockMvc.perform(get("/api/productos")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "name,desc"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductosCategoriaAndPagination() throws Exception {
        mockMvc.perform(get("/api/productos")
                .param("categoria", categoria.getCategoriaId().toString())
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductosOnlyCategoria() throws Exception {
        mockMvc.perform(get("/api/productos")
                .param("categoria", categoria.getCategoriaId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateProductosEmptyList() throws Exception {
        mockMvc.perform(post("/api/productos/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testCreateProductosInvalid() throws Exception {
        Producto invalido = new Producto();
        mockMvc.perform(post("/api/productos/batch")
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
        mockMvc.perform(post("/api/productos/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of(p1, p2))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testActivarProductoNotFound() throws Exception {
        mockMvc.perform(put("/api/productos/999999/activar"))
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
        mockMvc.perform(post("/api/productos/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of(valido, invalido))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetProductosNoResults() throws Exception {
        mockMvc.perform(get("/api/productos")
                .param("categoria", "99999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetProductosPaginationNoResults() throws Exception {
        mockMvc.perform(get("/api/productos")
                .param("page", "100")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductosParametrosInvalidos() throws Exception {
        // page negativo
        mockMvc.perform(get("/api/productos")
                .param("page", "-1")
                .param("size", "10"))
                .andExpect(status().isBadRequest());
        // size cero
        mockMvc.perform(get("/api/productos")
                .param("page", "0")
                .param("size", "0"))
                .andExpect(status().isBadRequest());
        // sort mal formado
        mockMvc.perform(get("/api/productos")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "invalido,xyz"))
                .andExpect(status().isOk()); // El controlador ignora el sort inválido, pero se puede ajustar si lanza error
    }

    @Test
    void testUpdateProductoIdNegativo() throws Exception {
        Producto update = new Producto();
        update.setName("Negativo");
        update.setDescription("Desc");
        update.setBrand("Marca");
        update.setBasePrice(1000);
        update.setIsActive(true);
        mockMvc.perform(put("/api/productos/-5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProductoIdNegativo() throws Exception {
        mockMvc.perform(delete("/api/productos/-10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActivarProductoYaActivo() throws Exception {
        // El producto ya está activo
        mockMvc.perform(put("/api/productos/" + producto.getProductId() + "/activar"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductosCombinaciones() throws Exception {
        // paginación + categoría + sort
        mockMvc.perform(get("/api/productos")
                .param("categoria", categoria.getCategoriaId().toString())
                .param("page", "0")
                .param("size", "2")
                .param("sort", "name,asc"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateProductoCamposNulos() throws Exception {
        Producto invalido = new Producto();
        invalido.setName(null);
        invalido.setDescription(null);
        invalido.setBrand(null);
        invalido.setBasePrice(null);
        invalido.setIsActive(null);
        invalido.setCategoria(null);
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProductosListaGrande() throws Exception {
        java.util.List<Producto> lista = new java.util.ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Producto p = new Producto();
            p.setName("Lote" + i);
            p.setDescription("Desc" + i);
            p.setBrand("Marca" + i);
            p.setBasePrice(10 + i);
            p.setIsActive(true);
            p.setCategoria(categoria);
            lista.add(p);
        }
        mockMvc.perform(post("/api/productos/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lista)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(20));
    }

    @Test
    void testCountByCategoriaSinProductos() throws Exception {
        // Crear una categoría sin productos
        Categoria vacia = new Categoria();
        vacia.setName("Vacia");
        vacia.setDescription("Sin productos");
        vacia.setIsActive(true);
        vacia = categoriaRepository.save(vacia);
        mockMvc.perform(get("/api/productos/count-by-categoria"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductosTodasCombinaciones() throws Exception {
        Faker faker = new Faker();
        // Sin parámetros
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());
        // Solo categoría
        Categoria cat = categoriaRepository.findAll().get(0);
        mockMvc.perform(get("/api/productos").param("categoria", cat.getCategoriaId().toString()))
                .andExpect(status().isOk());
        // Solo paginación
        mockMvc.perform(get("/api/productos").param("page", "0").param("size", "3"))
                .andExpect(status().isOk());
        // Paginación + sort
        mockMvc.perform(get("/api/productos").param("page", "0").param("size", "3").param("sort", "name,desc"))
                .andExpect(status().isOk());
        // Paginación + categoría
        mockMvc.perform(get("/api/productos").param("page", "0").param("size", "3").param("categoria", cat.getCategoriaId().toString()))
                .andExpect(status().isOk());
        // Paginación + categoría + sort
        mockMvc.perform(get("/api/productos").param("page", "0").param("size", "3").param("categoria", cat.getCategoriaId().toString()).param("sort", "name,asc"))
                .andExpect(status().isOk());
        // Parámetros inválidos
        mockMvc.perform(get("/api/productos").param("page", "-1").param("size", "10"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/productos").param("page", "0").param("size", "0"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/productos").param("page", "0").param("size", "10").param("sort", "invalido,xyz"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateProductosVariados() throws Exception {
        Faker faker = new Faker();
        // Lista vacía
        mockMvc.perform(post("/api/productos/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(0));
        // Todos válidos
        Categoria cat = categoriaRepository.findAll().get(0);
        Producto p1 = new Producto();
        p1.setName(faker.commerce().productName());
        p1.setDescription(faker.lorem().sentence());
        p1.setBrand(faker.company().name());
        p1.setBasePrice(faker.number().numberBetween(10, 1000));
        p1.setIsActive(true);
        p1.setCategoria(cat);
        Producto p2 = new Producto();
        p2.setName(faker.commerce().productName());
        p2.setDescription(faker.lorem().sentence());
        p2.setBrand(faker.company().name());
        p2.setBasePrice(faker.number().numberBetween(10, 1000));
        p2.setIsActive(false);
        p2.setCategoria(cat);
        mockMvc.perform(post("/api/productos/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of(p1, p2))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));
        // Uno inválido
        Producto invalido = new Producto();
        mockMvc.perform(post("/api/productos/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of(p1, invalido))))
                .andExpect(status().isBadRequest());
        // Mezcla de categorías
        Categoria cat2 = categoriaRepository.findAll().get(1);
        Producto p3 = new Producto();
        p3.setName(faker.commerce().productName());
        p3.setDescription(faker.lorem().sentence());
        p3.setBrand(faker.company().name());
        p3.setBasePrice(faker.number().numberBetween(10, 1000));
        p3.setIsActive(true);
        p3.setCategoria(cat2);
        mockMvc.perform(post("/api/productos/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.List.of(p1, p3))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testUpdateProductoCaminos() throws Exception {
        Faker faker = new Faker();
        // Existente
        producto.setDescription("Actualizado " + faker.lorem().word());
        mockMvc.perform(put("/api/productos/" + producto.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(producto.getDescription()));
        // Inexistente
        Producto update = new Producto();
        update.setName("NoExiste");
        update.setDescription("Desc");
        update.setBrand("Marca");
        update.setBasePrice(1000);
        update.setIsActive(true);
        mockMvc.perform(put("/api/productos/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
        // ID negativo
        mockMvc.perform(put("/api/productos/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProductoCaminos() throws Exception {
        // Existente
        mockMvc.perform(delete("/api/productos/" + producto.getProductId()))
                .andExpect(status().isNoContent());
        // Inexistente
        mockMvc.perform(delete("/api/productos/999999"))
                .andExpect(status().isNotFound());
        // ID negativo
        mockMvc.perform(delete("/api/productos/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActivarProductoCaminos() throws Exception {
        // Inactivo
        producto.setIsActive(false);
        productoRepository.save(producto);
        mockMvc.perform(put("/api/productos/" + producto.getProductId() + "/activar"))
                .andExpect(status().isOk());
        // Ya activo
        mockMvc.perform(put("/api/productos/" + producto.getProductId() + "/activar"))
                .andExpect(status().isOk());
        // Inexistente
        mockMvc.perform(put("/api/productos/999999/activar"))
                .andExpect(status().isNotFound());
        // ID negativo
        mockMvc.perform(put("/api/productos/-1/activar"))
                .andExpect(status().isNotFound());
    }
} 