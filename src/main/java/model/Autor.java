package com.aluracursos.literatura.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer anioNacimiento;
    private Integer anioFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor() {}

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.anioNacimiento = datosAutor.anioNacimiento();
        this.anioFallecimiento = datosAutor.anioFallecimiento();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Integer getAnioNacimiento() { return anioNacimiento; }
    public Integer getAnioFallecimiento() { return anioFallecimiento; }
    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }

    @Override
    public String toString() {
        return "\n----- AUTOR -----" +
                "\nNombre: " + nombre +
                "\nAño de nacimiento: " + anioNacimiento +
                "\nAño de fallecimiento: " + anioFallecimiento +
                "\n-----------------";
    }
}