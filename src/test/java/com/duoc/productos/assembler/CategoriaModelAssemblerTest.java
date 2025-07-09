package com.duoc.productos.assembler;

import com.duoc.productos.model.Categoria;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaModelAssemblerTest {
    @Test
    void toModel_debeRetornarEntityModelConLinks() {
        Categoria categoria = new Categoria();
        categoria.setCategoriaId(1L);
        categoria.setName("Test Categoria");

        CategoriaModelAssembler assembler = new CategoriaModelAssembler();
        EntityModel<Categoria> model = assembler.toModel(categoria);

        assertNotNull(model);
        assertEquals(categoria, model.getContent());
        assertTrue(model.getLinks().hasLink("self"));
        assertTrue(model.getLinks().hasLink("categorias"));
    }
} 