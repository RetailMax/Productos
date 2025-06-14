package com.duoc.productos;

import net.datafaker.Faker;
import com.duoc.productos.model.Producto;
import com.duoc.productos.model.Categoria;

public class FakerFactory {
    private static final Faker faker = new Faker();

    public static Producto fakeProducto() {
        Producto producto = new Producto();
        producto.setName(faker.commerce().productName());
        producto.setDescription(faker.lorem().sentence());
        producto.setBrand(faker.company().name());
        producto.setBasePrice(faker.number().numberBetween(1000, 100000));
        producto.setIsActive(true);
        producto.setCategoria(fakeCategoria());
        return producto;
    }

    public static Categoria fakeCategoria() {
        Categoria categoria = new Categoria();
        categoria.setName(faker.commerce().department());
        categoria.setDescription(faker.lorem().sentence());
        categoria.setIsActive(true);
        return categoria;
    }

    // Puedes agregar más generadores aquí para otros modelos
} 