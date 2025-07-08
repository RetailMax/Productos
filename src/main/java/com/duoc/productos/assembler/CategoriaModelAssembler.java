package com.duoc.productos.assembler;

import com.duoc.productos.model.Categoria;
import com.duoc.productos.controller.CategoriaController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<Categoria, EntityModel<Categoria>> {
    @Override
    public EntityModel<Categoria> toModel(Categoria categoria) {
        return EntityModel.of(categoria,
            linkTo(methodOn(CategoriaController.class).getCategoriaById(categoria.getCategoriaId())).withSelfRel(),
            linkTo(methodOn(CategoriaController.class).getAllCategorias()).withRel("categorias")
        );
    }
} 