package com.alura.literalura.service;

import com.alura.literalura.model.Autor;
import com.alura.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    @Autowired
    private AutorRepository authorRepository;

    public Optional<Autor> findByName(String text) {
        return authorRepository.findByNombre(text);
    }

    public List<Autor> searchAuthorsByYear(int year) {
        return authorRepository.buscarAutoresPorAnio(year);
    }

    public Autor saveAuthor(Autor author) {
        return authorRepository.save(author);
    }

    public List<Autor> findAllAuthors() {
        return authorRepository.findAll();
    }
}
