package com.duoc.productos.controller;

import com.duoc.productos.model.Categoria;
import com.duoc.productos.service.CategoriaService;
import com.duoc.productos.assembler.CategoriaModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;
    
    @Autowired
    private CategoriaModelAssembler categoriaModelAssembler;
    
    @GetMapping
    public List<Categoria> getAllCategorias() {
        return categoriaService.getAllCategorias();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        return categoriaService.getCategoriaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) {
        Categoria creada = categoriaService.createCategoria(categoria);
        return ResponseEntity.status(201).body(creada);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        try {
            Categoria updatedCategoria = categoriaService.updateCategoria(id, categoria);
            return ResponseEntity.ok(updatedCategoria);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        try {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<Categoria> searchCategorias(@RequestParam("q") String query) {
        return categoriaService.searchCategorias(query);
    }

    @GetMapping("/exists")
    public java.util.Map<String, Boolean> existsByName(@RequestParam("name") String name) {
        boolean exists = categoriaService.existsByName(name);
        return java.util.Collections.singletonMap("exists", exists);
    }
}
