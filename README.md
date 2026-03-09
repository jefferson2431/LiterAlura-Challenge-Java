# LiterAlura 📚

Catálogo de libros interactivo desarrollado en Java con Spring Boot,
que consume la API Gutendex y almacena datos en PostgreSQL.

## Tecnologías utilizadas
- Java 17
- Spring Boot 3.5.11
- Spring Data JPA
- PostgreSQL
- Jackson (ObjectMapper)
- API Gutendex

## Funcionalidades
- Buscar libro por título (API Gutendex)
- Listar todos los libros registrados
- Listar autores registrados
- Listar autores vivos en determinado año
- Listar libros por idioma
- Estadísticas de libros por idioma
- Top 10 libros más descargados
- Buscar autor por nombre

## ️ Configuración de base de datos
Crea una base de datos PostgreSQL llamada `literatura` y configura
tus credenciales en `src/main/resources/application.properties`:
```
spring.datasource.url=jdbc:postgresql://localhost/literatura
spring.datasource.username=postgres
spring.datasource.password=TU_CONTRASEÑA
```

##  Cómo ejecutar
1. Clona el repositorio
2. Configura PostgreSQL
3. Abre en IntelliJ IDEA
4. Ejecuta `LiteraturaApplication.java`

## 📌 Ejemplo de uso
```
========== LITERALURA ==========
1 - Buscar libro por título
2 - Listar libros registrados
3 - Listar autores registrados
4 - Listar autores vivos en un determinado año
5 - Listar libros por idioma
6 - Estadísticas de libros por idioma
7 - Top 10 libros más descargados
8 - Buscar autor por nombre
0 - Salir
=================================
```

## 👨‍💻 Autor
Yeferson Jaime Galvis - Challenge ONE | Java Back End - Alura + Oracle