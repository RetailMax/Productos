package com.duoc.productos.controller;

import com.duoc.productos.model.Producto;
import com.duoc.productos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.Validator;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.duoc.productos.assembler.ProductoModelAssembler;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@Validated
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired(required = false)
    @Qualifier("mvcValidator")
    private Validator validator;
    
    @Autowired
    private ProductoModelAssembler productoModelAssembler;
    
    @GetMapping
    public CollectionModel<EntityModel<Producto>> getProductos(
            @RequestParam(value = "categoria", required = false) Long categoriaId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        List<Producto> productos;
        if (page != null && size != null) {
            org.springframework.data.domain.Pageable pageable;
            if (sort != null) {
                String[] sortParams = sort.split(",");
                String sortField = sortParams[0];
                org.springframework.data.domain.Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? org.springframework.data.domain.Sort.Direction.DESC : org.springframework.data.domain.Sort.Direction.ASC;
                pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(direction, sortField));
            } else {
                pageable = org.springframework.data.domain.PageRequest.of(page, size);
            }
            if (categoriaId != null) {
                productos = productoService.findByCategoriaPaged(categoriaId, pageable).getContent();
            } else {
                productos = productoService.findAllActivePaged(pageable).getContent();
            }
        } else if (categoriaId != null) {
            productos = productoService.findByCategoria(categoriaId);
        } else {
            productos = productoService.findAll();
        }
        List<EntityModel<Producto>> productosModel = productos.stream()
            .map(productoModelAssembler::toModel)
            .toList();
        return CollectionModel.of(productosModel,
            linkTo(methodOn(ProductoController.class).getProductos(null, null, null, null)).withSelfRel()
        );
    }
    
    @GetMapping("/{id}")
    public EntityModel<Producto> getProductoById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(productoModelAssembler::toModel)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));
    }
    
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Producto> createProducto(@RequestBody @Valid Producto producto) {
        Producto creado = productoService.save(producto);
        return ResponseEntity.status(201).body(creado);
    }
    
    @PostMapping(value = "/batch", consumes = "application/json")
    public ResponseEntity<List<Producto>> createProductos(@RequestBody List<@Valid Producto> productos) {
        if (validator != null) {
            for (Producto p : productos) {
                var errors = new org.springframework.validation.BeanPropertyBindingResult(p, "producto");
                validator.validate(p, errors);
                if (errors.hasErrors()) {
                    throw new RuntimeException(new org.springframework.web.bind.MethodArgumentNotValidException(null, errors));
                }
            }
        }
        List<Producto> creados = productos.stream()
                .map(productoService::save)
                .toList();
        return ResponseEntity.status(201).body(creados);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            if (!productoService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            producto.setProductId(id);
            Producto updatedProducto = productoService.save(producto);
            return ResponseEntity.ok(updatedProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (!productoService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoService.deleteProductoReal(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public List<Producto> searchProductos(@RequestParam("q") String query) {
        return productoService.searchProductos(query);
    }
    
    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activarProducto(@PathVariable Long id) {
        boolean result = productoService.activarProducto(id);
        return result ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/inactivos")
    public List<Producto> getProductosInactivos() {
        return productoService.findInactivos();
    }

    @GetMapping("/count-by-categoria")
    public List<Object[]> countByCategoria() {
        return productoService.countByCategoria();
    }

    @GetMapping("/exists")
    public java.util.Map<String, Boolean> existsByName(@RequestParam("name") String name) {
        boolean exists = productoService.existsByName(name);
        return java.util.Collections.singletonMap("exists", exists);
    }

    @PutMapping("/{id}/toggle-active")
    public ResponseEntity<Producto> toggleActiveProducto(@PathVariable Long id) {
        return productoService.findById(id)
                .map(producto -> {
                    producto.setIsActive(!Boolean.TRUE.equals(producto.getIsActive()));
                    Producto actualizado = productoService.save(producto);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 