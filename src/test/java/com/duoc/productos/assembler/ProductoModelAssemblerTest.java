package com.duoc.productos.assembler;

import com.duoc.productos.model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

class ProductoModelAssemblerTest {
    @Test
    void toModel_debeRetornarEntityModelConLinks() {
        Producto producto = new Producto();
        producto.setProductId(1L);
        producto.setName("Test Producto");

        ProductoModelAssembler assembler = new ProductoModelAssembler();
        EntityModel<Producto> model = assembler.toModel(producto);

        assertNotNull(model);
        assertEquals(producto, model.getContent());
        assertTrue(model.getLinks().hasLink("self"));
        assertTrue(model.getLinks().hasLink("productos"));
        assertTrue(model.getLinks().hasLink("update"));
        assertTrue(model.getLinks().hasLink("delete"));
    }
} 