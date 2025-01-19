package com.alura.literalura.view;

import com.alura.literalura.model.Autor;
import com.alura.literalura.service.AuthorService;
import com.alura.literalura.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class Menu {
    private final Scanner teclado = new Scanner(System.in);
    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public Menu(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    public void showMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ---------------MENÚ----------------
                    Escriba el número de la opción que desea ejecutar:
                    1 - Buscar libro por título
                    2 - Listar todos los libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Top 10 de los libros más descargados
                    7 - Estadísticas de los libros registrados
                    
                    0 - Salir
                    ------------------------------------
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            String mensajeOpcionInvalida = "Opción inválida. Intente nuevamente.";
            switch (opcion) {
                case 1:
                    saveBook();
                    break;
                case 2:
                    listRegisteredBooks();
                    break;
                case 3:
                    listRegisteredAuthors();
                    break;
                case 4:
                    listLivingAuthorsByYear();
                    break;
                case 5:
                    listBooksByLanguage();
                    break;
                case 6:
                    topMostDownloadedBooks();
                    break;
                case 7:
                    showRating();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println(mensajeOpcionInvalida);
            }
        }
    }

    public void saveBook() {
        System.out.println("Ingrese el título del libro que desea registrar:");
        var title = teclado.nextLine();
        bookService.saveBook(title);
    }

    public void listRegisteredBooks() {
        System.out.println("---Lista de libros registrados---");
        bookService.listRegisteredBooks();
    }

    public void listRegisteredAuthors(){
        System.out.println("---Lista de autores registrados---");
        bookService.listRegisteredAuthors();
    }

    public void listLivingAuthorsByYear(){
        System.out.println("Ingrese un año para buscar a los autores que seguían con vida");
        var year = teclado.nextInt();
        List<Autor> livingAuthors = authorService.searchAuthorsByYear(year);
        System.out.println("---Autores registrados vivos en " + year + "---");
        livingAuthors.forEach(a -> System.out.println("\n" + a.toString()));
    }

    public void listBooksByLanguage() {
        System.out.println(
                """
                        --------------------
                        Idiomas disponibles:
                        es - Español
                        en - Inglés
                        pt - Portugués
                        fr - Francés
                        it - Italiano
                        de - Alemán
                        ---------------------
                        """
        );
        System.out.println("Ingrese el idioma que desea buscar");
        var chosenLanguage = teclado.nextLine();
        bookService.listBooksByLanguage(chosenLanguage);
    }

    public void topMostDownloadedBooks() {
        System.out.println("---Top 10 de los libros más descargados---");
        bookService.topMostDownloadedBooks();
    }

    public void showRating() {
        System.out.println("---Estadísticas de los libros registrados---");
        bookService.showRating();
    }
}
