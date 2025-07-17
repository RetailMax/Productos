package com.duoc.productos.service;

import com.duoc.productos.model.Producto;
import com.duoc.productos.repository.ProductoRepository;
import com.duoc.productos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Crear o actualizar producto
    public Producto save(Producto producto) {
        if (producto.getCategoria() != null && producto.getCategoria().getCategoriaId() != null) {
            categoriaRepository.findById(producto.getCategoria().getCategoriaId())
                .ifPresent(producto::setCategoria);
        }
        return productoRepository.save(producto);
    }

    // Buscar producto por ID
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    // Listar todos los productos
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    // Eliminar producto por ID (borrado lógico)
    public void deleteById(Long id) {
        productoRepository.findById(id).ifPresent(producto -> {
            producto.setIsActive(false);
            productoRepository.save(producto);
        });
    }

    // Verificar si existe producto por ID
    public boolean existsById(Long id) {
        return productoRepository.existsById(id);
    }

    // Búsqueda por texto en nombre, descripción o marca
    public List<Producto> searchProductos(String query) {
        return productoRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrBrandContainingIgnoreCase(query, query, query);
    }

    // Listar productos por categoría (solo activos)
    public List<Producto> findByCategoria(Long categoriaId) {
        return productoRepository.findByCategoria_CategoriaIdAndIsActiveTrue(categoriaId);
    }

    // Listar productos activos con paginación y ordenamiento
    public Page<Producto> findAllActivePaged(Pageable pageable) {
        return productoRepository.findByIsActiveTrue(pageable);
    }

    // Listar productos por categoría con paginación y ordenamiento
    public Page<Producto> findByCategoriaPaged(Long categoriaId, Pageable pageable) {
        return productoRepository.findByCategoria_CategoriaIdAndIsActiveTrue(categoriaId, pageable);
    }

    // Activar producto (borrado lógico inverso)
    public boolean activarProducto(Long id) {
        return productoRepository.findById(id).map(producto -> {
            producto.setIsActive(true);
            productoRepository.save(producto);
            return true;
        }).orElse(false);
    }

    // Obtener productos inactivos
    public List<Producto> findInactivos() {
        return productoRepository.findByIsActiveFalse();
    }

    // Contar productos activos por categoría
    public List<Object[]> countByCategoria() {
        return productoRepository.countActiveByCategoria();
    }

    // Validar existencia de producto por nombre
    public boolean existsByName(String name) {
        return productoRepository.existsByNameIgnoreCase(name);
    }

    // Borrado real de producto
    public void deleteProductoReal(Long id) {
        productoRepository.deleteById(id);
    }

    // Listar todos los productos (activos e inactivos) con paginación
    public Page<Producto> findAllPaged(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }
}
