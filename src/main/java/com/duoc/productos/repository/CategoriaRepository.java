package com.duoc.productos.repository;

import com.duoc.productos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
    boolean existsByNameIgnoreCase(String name);
} 