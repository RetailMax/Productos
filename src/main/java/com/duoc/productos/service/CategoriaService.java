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
    
    public List<Categoria> getCategoriasInactivas() {
        return categoriaRepository.findByIsActiveFalse();
    }
    public Categoria activarCategoria(Long id, boolean activar) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoria.setIsActive(activar);
        return categoriaRepository.save(categoria);
    }
    
    public List<Categoria> crearCategoriasEnLote(List<Categoria> categorias) {
        return categoriaRepository.saveAll(categorias);
    }
    
} 