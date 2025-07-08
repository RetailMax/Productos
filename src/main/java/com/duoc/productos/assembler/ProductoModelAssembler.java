package com.duoc.productos.assembler;

import com.duoc.productos.model.Producto;
import com.duoc.productos.controller.ProductoController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {
    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
            linkTo(methodOn(ProductoController.class).getProductoById(producto.getProductId())).withSelfRel(),
            linkTo(methodOn(ProductoController.class).getProductos(null, null, null, null)).withRel("productos"),
            linkTo(methodOn(ProductoController.class).updateProducto(producto.getProductId(), producto)).withRel("update"),
            linkTo(methodOn(ProductoController.class).deleteProducto(producto.getProductId())).withRel("delete")
        );
    }
} 