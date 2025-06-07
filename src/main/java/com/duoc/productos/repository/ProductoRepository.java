package com.duoc.productos.repository;

import com.duoc.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Aquí puedes agregar métodos personalizados si los necesitas

    // Búsqueda por texto en nombre, descripción o marca (ignorando mayúsculas/minúsculas)
    List<Producto> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrBrandContainingIgnoreCase(String name, String description, String brand);

    // Buscar productos por categoría y activos
    List<Producto> findByCategoria_CategoriaIdAndIsActiveTrue(Long categoriaId);

    // Buscar productos activos con paginación
    Page<Producto> findByIsActiveTrue(Pageable pageable);

    // Buscar productos por categoría y activos con paginación
    Page<Producto> findByCategoria_CategoriaIdAndIsActiveTrue(Long categoriaId, Pageable pageable);

    // Buscar productos inactivos
    List<Producto> findByIsActiveFalse();

    // Contar productos activos por categoría
    @Query("SELECT p.categoria.categoriaId, COUNT(p) FROM Producto p WHERE p.isActive = true GROUP BY p.categoria.categoriaId")
    List<Object[]> countActiveByCategoria();

    // Validar existencia de producto por nombre (ignorando mayúsculas/minúsculas)
    boolean existsByNameIgnoreCase(String name);
}
