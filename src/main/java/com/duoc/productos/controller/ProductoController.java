package com.duoc.productos.controller;

import com.duoc.productos.model.Producto;
import com.duoc.productos.service.ProductoService;
import com.duoc.productos.assemblers.ProductoModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
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
        // Si se solicita paginación
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
            // Solo filtrar por categoría
            productos = productoService.findByCategoria(categoriaId);
        } else {
            // Listar todos los productos activos
            productos = productoService.findAll().stream().filter(Producto::getIsActive).toList();
        }
        List<EntityModel<Producto>> productosModel = productos.stream()
                .map(productoModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productosModel);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> getProductoById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(productoModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping(consumes = "application/json")
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.save(producto);
    }
    
    @PostMapping(value = "/batch", consumes = "application/json")
    public List<Producto> createProductos(@RequestBody List<Producto> productos) {
        return productos.stream()
                .map(productoService::save)
                .toList();
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
        try {
            if (!productoService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
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
    public boolean existsByName(@RequestParam("name") String name) {
        return productoService.existsByName(name);
    }
} 