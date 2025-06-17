package com.duoc.productos.controller;

import com.duoc.productos.model.Categoria;
import com.duoc.productos.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;
    
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
    public Categoria createCategoria(@RequestBody Categoria categoria) {
        return categoriaService.createCategoria(categoria);
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
    
    @GetMapping("/inactivas")
    public List<Categoria> getCategoriasInactivas() {
        return categoriaService.getCategoriasInactivas();
    }
    
} 