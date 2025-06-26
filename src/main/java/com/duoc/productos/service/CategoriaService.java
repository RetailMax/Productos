package com.duoc.productos.service;

import com.duoc.productos.model.Categoria;
import com.duoc.productos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    // Constructor para tests
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }
    
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }
    
    public Optional<Categoria> getCategoriaById(Long id) {
        return categoriaRepository.findById(id);
    }
    
    public Categoria createCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }
    
    public Categoria updateCategoria(Long id, Categoria categoriaDetails) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        categoria.setName(categoriaDetails.getName());
        categoria.setDescription(categoriaDetails.getDescription());
        categoria.setIsActive(categoriaDetails.getIsActive());
        
        return categoriaRepository.save(categoria);
    }
    
    public void deleteCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoriaRepository.delete(categoria);
    }

    public List<Categoria> searchCategorias(String query) {
        return categoriaRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }

    public boolean existsByName(String name) {
        return categoriaRepository.existsByNameIgnoreCase(name);
    }
    
} 