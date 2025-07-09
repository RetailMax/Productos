# Productos

## Docker configuración

1. Crear app.jar
```
./mvnw clean package
```

2. Iniciar el proyecto
```
docker compose up
```

## Carga batch de productos

La aplicación permite cargar productos de forma masiva (batch) a través de la interfaz web. Para ello:

- Haz clic en el botón "Carga batch" en la interfaz.
- Pega un array JSON de productos siguiendo el formato de ejemplo.
- Solo se permite la carga batch de productos (no de categorías).

### Formato de ejemplo

Puedes usar el archivo de ejemplo `src/main/resources/static/batch-ejemplo-productos.json` como referencia. Ejemplo:

```
[
  {
    "name": "Notebook Lenovo ThinkPad",
    "description": "Notebook profesional con procesador Intel i5 y 8GB RAM.",
    "brand": "Lenovo",
    "basePrice": 499990,
    "imageUrl": "https://media.falabella.com/falabellaCL/129185203_01/w=1500,h=1500,fit=pad",
    "categoria": { "categoriaId": 435 }
  },
  // ... más productos ...
]
```

- El campo `categoria.categoriaId` debe corresponder a una categoría existente y activa.
- Si algún producto es inválido, la carga batch será rechazada completamente (transaccional).

---

Para más detalles, revisa la documentación en la interfaz (Swagger UI) o consulta los archivos de ejemplo incluidos en el proyecto.