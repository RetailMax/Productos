package com.duoc.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(scanBasePackages = "com.duoc.productos")

public class ProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductosApplication.class, args);
	}

}
