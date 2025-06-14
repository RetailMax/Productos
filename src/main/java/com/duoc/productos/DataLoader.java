package com.duoc.productos;

import com.duoc.productos.model.Producto;
import com.duoc.productos.model.Categoria;
import com.duoc.productos.repository.ProductoRepository;
import com.duoc.productos.repository.CategoriaRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // Generar categorías
        for (int i = 0; i < 5; i++) {
            Categoria categoria = new Categoria();
            categoria.setName(faker.commerce().department());
            categoria.setDescription(faker.lorem().sentence());
            categoria.setIsActive(true);
            categoriaRepository.save(categoria);
        }
        List<Categoria> categorias = categoriaRepository.findAll();

        // Generar productos
        for (int i = 0; i < 20; i++) {
            Producto producto = new Producto();
            producto.setName(faker.commerce().productName());
            producto.setDescription(faker.lorem().sentence());
            producto.setBrand(faker.company().name());
            producto.setBasePrice(faker.number().numberBetween(1000, 100000));
            producto.setIsActive(true);
            producto.setCategoria(categorias.get(random.nextInt(categorias.size())));
            productoRepository.save(producto);
        }
    }
} 