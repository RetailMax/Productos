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

Ruta de Jacoco (Local):
E:\CODE\LEARNING\DUOC - ANALISTA PROGRAMADOR\FULL STACK\Fullstack - Productos\Productos\target\site\jacoco\index.html

## Despliegue en Google Cloud Run

Sigue estos pasos para reconstruir y desplegar la aplicación después de realizar cambios:

1. **Construir el JAR:**

   ```sh
   ./mvnw clean package -DskipTests
   ```

2. **Construir la imagen Docker:**

   ```sh
   docker build -t gcr.io/productos-microservicio/productos:latest .
   ```

3. **Subir la imagen a Google Container Registry:**

   ```sh
   docker push gcr.io/productos-microservicio/productos:latest
   ```

4. **Desplegar en Cloud Run:**

   ```sh
   gcloud run deploy productos --image gcr.io/productos-microservicio/productos:latest --platform managed --region southamerica-west1 --allow-unauthenticated
   ```

- La aplicación quedará disponible en: [https://productos-459829548521.southamerica-west1.run.app/](https://productos-459829548521.southamerica-west1.run.app/)

- Documentación Swagger UI: [https://productos-459829548521.southamerica-west1.run.app/doc/swagger-ui/index.html](https://productos-459829548521.southamerica-west1.run.app/doc/swagger-ui/index.html)

**Notas:**
- Asegúrate de tener configurado el proyecto correcto en Google Cloud (`productos-microservicio`).
- Si es la primera vez, ejecuta `gcloud auth login` y `gcloud auth configure-docker`.
- Puedes modificar el nombre del servicio (`productos`) o la región si lo necesitas.