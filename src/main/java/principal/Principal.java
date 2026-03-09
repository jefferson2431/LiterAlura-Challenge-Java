package com.aluracursos.literatura.principal;

import com.aluracursos.literatura.model.*;
import com.aluracursos.literatura.repository.AutorRepository;
import com.aluracursos.literatura.repository.LibroRepository;
import com.aluracursos.literatura.service.ConsumoAPI;
import com.aluracursos.literatura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            String menu = """
                    \n========== LITERALURA ==========
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
                    """;
            System.out.println(menu);
            System.out.print("Elija una opción: ");

            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠ Opción inválida.");
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibroPorTitulo();
                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosEnAnio();
                case 5 -> listarLibrosPorIdioma();
                case 6 -> estadisticasPorIdioma();
                case 7 -> top10LibrosMasDescargados();
                case 8 -> buscarAutorPorNombre();
                case 0 -> System.out.println("\n¡Hasta luego!");
                default -> System.out.println("⚠ Opción inválida.");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.print("\nIngresa el título del libro: ");
        String titulo = teclado.nextLine();
        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + titulo.replace(" ", "+"));
        DatosRespuesta datos = conversor.obtenerDatos(json, DatosRespuesta.class);

        if (datos.resultados().isEmpty()) {
            System.out.println("Libro no encontrado.");
            return;
        }

        DatosLibro datosLibro = datos.resultados().get(0);
        Optional<Libro> libroExistente = libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo());

        if (libroExistente.isPresent()) {
            System.out.println("\n⚠ Este libro ya está registrado:");
            System.out.println(libroExistente.get());
            return;
        }

        Autor autor = new Autor(datosLibro.autores().get(0));
        autor = autorRepository.save(autor);

        Libro libro = new Libro(datosLibro);
        libro.setAutor(autor);
        libroRepository.save(libro);

        System.out.println("\n✅ Libro guardado exitosamente:");
        System.out.println(libro);
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados.");
            return;
        }
        System.out.println("\n===== LIBROS REGISTRADOS =====");
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("\nNo hay autores registrados.");
            return;
        }
        System.out.println("\n===== AUTORES REGISTRADOS =====");
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosEnAnio() {
        System.out.print("\nIngresa el año: ");
        try {
            int anio = Integer.parseInt(teclado.nextLine());
            List<Autor> autores = autorRepository.findAutoresVivosEnAnio(anio);
            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio);
                return;
            }
            System.out.println("\n===== AUTORES VIVOS EN " + anio + " =====");
            autores.forEach(System.out::println);
        } catch (NumberFormatException e) {
            System.out.println("⚠ Año inválido.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                \nIdiomas disponibles:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);
        System.out.print("Ingresa el código del idioma: ");
        String idioma = teclado.nextLine();
        List<Libro> libros = libroRepository.findByIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma.");
            return;
        }
        System.out.println("\n===== LIBROS EN IDIOMA: " + idioma.toUpperCase() + " =====");
        libros.forEach(System.out::println);
    }

    private void estadisticasPorIdioma() {
        System.out.println("""
                \nIdiomas disponibles:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);
        System.out.print("Ingresa el código del idioma: ");
        String idioma = teclado.nextLine();
        Long cantidad = libroRepository.countByIdioma(idioma);
        System.out.println("\nCantidad de libros en " + idioma.toUpperCase() + ": " + cantidad);
    }

    private void top10LibrosMasDescargados() {
        List<Libro> libros = libroRepository.findAll();
        System.out.println("\n===== TOP 10 LIBROS MÁS DESCARGADOS =====");
        libros.stream()
                .sorted((a, b) -> b.getNumeroDescargas() - a.getNumeroDescargas())
                .limit(10)
                .forEach(System.out::println);
    }

    private void buscarAutorPorNombre() {
        System.out.print("\nIngresa el nombre del autor: ");
        String nombre = teclado.nextLine();
        List<Autor> autores = autorRepository.findByNombreContainsIgnoreCase(nombre);
        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores con ese nombre.");
            return;
        }
        autores.forEach(System.out::println);
    }
}
