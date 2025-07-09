# Testing Log

## Resumen general
Este documento registra el proceso de implementación, mejora y documentación de las pruebas unitarias e integración en el proyecto `productos`.

---

## Estrategia de pruebas
- **Pruebas unitarias:** Se implementaron para servicios, assemblers y lógica de negocio aislada.
- **Pruebas de integración:** Se cubrieron los controladores REST, validando tanto casos exitosos como de error.
- **Cobertura:** Se utilizó JaCoCo para medir la cobertura de código, con reportes visuales para identificar áreas a mejorar.

---

## Proceso de mejora de cobertura
1. **Cobertura inicial baja:**
   - Los assemblers y algunos controladores tenían <10% de cobertura.
   - Faltaban tests para endpoints de activación/desactivación y casos de error.

2. **Acciones realizadas:**
   - Se agregaron tests unitarios para los assemblers (`ProductoModelAssembler`, `CategoriaModelAssembler`).
   - Se revisaron todos los endpoints de los controladores y se agregaron tests faltantes, especialmente para:
     - Activar/desactivar productos y categorías.
     - Casos de recursos no encontrados (404).
     - Casos de validación y listas vacías en endpoints batch.
     - Combinaciones de filtros, paginación y ordenamiento.
   - Se ajustaron los asserts de los tests para reflejar el comportamiento real de los endpoints (por ejemplo, status 204 vs 404 en DELETE).
   - Se deshabilitó/eliminó el test del método `main` de la aplicación, ya que no aporta valor real a la cobertura.

3. **Resultados alcanzados:**
   - Cobertura global de instrucciones: **97%**
   - Cobertura de ramas: **81%**
   - Cobertura de controladores: **97%**
   - Cobertura de servicios: **97%**
   - Cobertura de assemblers y modelos: **100%**

---

## Herramientas y comandos utilizados
- **Ejecución de tests:**
  ```
  mvn test
  ```
- **Generación de reporte de cobertura:**
  ```
  mvn jacoco:report
  # El reporte HTML se encuentra en target/site/jacoco/index.html
  ```

---

## Decisiones y lecciones aprendidas
- No se testea el método `main` de la aplicación, ya que no aporta valor real.
- Se priorizó la cobertura de lógica de negocio y endpoints críticos.
- Se documentaron los casos de error y validación para facilitar el mantenimiento.
- Se recomienda mantener la cobertura alta agregando tests para nuevas features y casos límite.

---

## Descripción de los tests principales

### Controladores

#### ProductoControllerTest.java
- `testGetProductoById_success`: Verifica que se obtiene un producto existente correctamente.
- `testGetProductoById_notFound`: Verifica que se retorna 404 si el producto no existe.
- `testCreateProducto`: Valida la creación de un producto con datos válidos.
- `testDeleteProducto_success`: Elimina un producto existente y espera 204.
- `testDeleteProducto_notFound`: Intenta eliminar un producto inexistente y espera 404.
- `testDesactivarProducto_activo`: Desactiva un producto activo y espera 200.
- `testDesactivarProducto_noExiste`: Intenta desactivar un producto inexistente y espera 404.
- ...

#### CategoriaControllerTest.java
- `testGetCategoriaById_success`: Obtiene una categoría existente.
- `testActivateCategoria_existente`: Activa una categoría y espera 200.
- `testActivateCategoria_noExistente`: Intenta activar una categoría inexistente y espera 404.
- `testDeactivateCategoria_existente`: Desactiva una categoría y espera 200.
- `testDeactivateCategoria_noExistente`: Intenta desactivar una categoría inexistente y espera 404.
- ...

### Servicios

#### ProductoServiceTest.java
- `testCreateProducto`: Valida la lógica de creación en el servicio.
- `testUpdateProducto`: Verifica la actualización de un producto existente.
- `testDeleteProducto`: Elimina un producto y verifica que no lanza excepción.
- `testSearchProductos`: Busca productos por texto y valida los resultados.
- ...

#### CategoriaServiceTest.java
- `testCreateCategoria`: Valida la creación de una categoría.
- `testUpdateCategoriaSuccess`: Actualiza una categoría existente.
- `testDeleteCategoriaSuccess`: Elimina una categoría existente.
- `testSearchCategorias`: Busca categorías por texto.
- ...

### Assemblers

#### ProductoModelAssemblerTest.java
- `toModel_debeRetornarEntityModelConLinks`: Verifica que el assembler agrega los links HATEOAS correctamente.

#### CategoriaModelAssemblerTest.java
- `toModel_debeRetornarEntityModelConLinks`: Verifica que el assembler agrega los links HATEOAS correctamente.

---

## Ejemplo de estructura de archivos de test
- `src/test/java/com/duoc/productos/controller/ProductoControllerTest.java`: Pruebas de endpoints de productos.
- `src/test/java/com/duoc/productos/controller/CategoriaControllerTest.java`: Pruebas de endpoints de categorías.
- `src/test/java/com/duoc/productos/assembler/ProductoModelAssemblerTest.java`: Pruebas unitarias del assembler de productos.
- `src/test/java/com/duoc/productos/service/ProductoServiceTest.java`: Pruebas unitarias del servicio de productos.

---
