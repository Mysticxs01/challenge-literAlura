package com.alura.literalura.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column (unique = true)
    private String titulo;

    @Enumerated(EnumType.STRING)
    private Idioma idioma;

    private Double numeroDescargas;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro() {
    }

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.autor = new Autor(datosLibro.autor().get(0));
        this.idioma = Idioma.fromString(datosLibro.idioma().get(0));
        this.numeroDescargas = datosLibro.numeroDescargas();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "-----------------------------------" + '\n' +
                "Título: " + titulo + '\n' +
                "Autor: " + (autor!= null ? autor.getNombre() : "Desconocido") + '\n' +
                "Idioma: " + (idioma == null ? "Desconocido" : idioma) + '\n' +
                "Número de descargas: " + (numeroDescargas != null ? numeroDescargas : 0)+ '\n';
    }

}
