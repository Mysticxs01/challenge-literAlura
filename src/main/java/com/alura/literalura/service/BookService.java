package com.alura.literalura.service;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private String mensajeOpcionInvalida = "Opción inválida. Intente nuevamente.";

    private final LibroRepository bookRepository;
    private final AuthorService authorService;
    private final ApiConsumption apiConsumption;

    @Autowired
    public BookService(LibroRepository bookRepository, ApiConsumption apiConsumption, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.apiConsumption = apiConsumption;
        this.authorService = authorService;
    }

    public void saveBook(String title) {
        DatosLibro bookData = apiConsumption.getBookData(title);
        if (bookData != null) {
            Optional<Autor> authorName = authorService.findByName(bookData.autor().get(0).nombre());

            Libro book = new Libro();
            book.setTitulo(bookData.titulo());
            book.setIdioma(Idioma.fromString(bookData.idioma().get(0)));
            book.setNumeroDescargas(bookData.numeroDescargas());

            if (authorName.isPresent()) {
                Autor autorExistente = authorName.get();
                book.setAutor(autorExistente);
            } else {
                Autor newAuthor = new Autor(bookData.autor().get(0));
                newAuthor = authorService.saveAuthor(newAuthor);
                book.setAutor(newAuthor);
            }
            try {
                Optional<Libro> libroExistente = bookRepository.findByTituloContainingIgnoreCase(bookData.titulo());
                if (libroExistente.isPresent()) {
                    System.out.println("El libro ya está registrado.");
                    return;
                }
                bookRepository.save(book);
                System.out.println("Libro registrado exitosamente");
                System.out.println(book);
            } catch (Exception e) {
                System.out.println("Libro registrado anteriormente");
            }
        } else {
            System.out.println(mensajeOpcionInvalida);
        }
    }

    public void listRegisteredBooks(){
        List<Autor> authors = authorService.findAllAuthors();
        if (authors.isEmpty()){
            System.out.println("Aún no hay autores registrados en la base de datos");
        } else {
            for (Autor author : authors) {
                List<Libro> librosPorAutor = bookRepository.findLibrosByAutorId(author.getId());
                System.out.println("\n" + author);
                System.out.println("Libros registrados: ");
                librosPorAutor.forEach(l -> System.out.println("- " + l.getTitulo()));
            }
        }
    }

    public void listRegisteredAuthors() {
        List<Autor> authors = authorService.findAllAuthors();
        if (authors.isEmpty()){
            System.out.println("Aún no hay autores registrados en la base de datos");
        } else {
            for (Autor author : authors) {
                List<Libro> booksByAuthor = bookRepository.findLibrosByAutorId(author.getId());
                System.out.println("\n" + author.toString());
                System.out.println("Libros registrados: ");
                booksByAuthor.forEach(l -> System.out.println("- " + l.getTitulo()));
            }
        }
    }

    public void listBooksByLanguage(String idiomaBuscado) {
        try {
            List<Libro> librosPorIdioma = bookRepository.findByIdioma(Idioma.fromString(idiomaBuscado));
            if (!librosPorIdioma.isEmpty()) {
                System.out.println("---Libros registrados publicados en " + Idioma.fromString(idiomaBuscado) + "---");
                librosPorIdioma.forEach(l -> System.out.println("\n" + l.toString()));
            } else {
                System.out.println("No se han encontrado libros en ese idioma.");
            }
        } catch (Exception e) {
            System.out.println(mensajeOpcionInvalida);
        }
    }

    public void topMostDownloadedBooks() {
        List<Libro> top10 = bookRepository.findTop10();
        top10.forEach(l -> System.out.println(l.toString()));
    }

    public void showRating() {
        List<Libro> books = bookRepository.findAll();
        DoubleSummaryStatistics estadisticas = books.stream()
                .filter(l -> l.getNumeroDescargas() > 0.0)
                .collect(Collectors.summarizingDouble(Libro::getNumeroDescargas));
        System.out.println("Máximo de descargas: " + estadisticas.getMax());
        System.out.println("Mínimo de descargas: " + estadisticas.getMin());
        System.out.println("Cantidad de libros registrados: " + estadisticas.getCount());
        System.out.println("Total de descargas de todos los libros registrados: " + estadisticas.getSum());
        System.out.println("Promedio de descargas: " + Math.round(estadisticas.getAverage()));
    }
}
