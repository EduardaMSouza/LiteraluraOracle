package com.eduarda.literalura.service;

import com.eduarda.literalura.dto.AuthorDTO;
import com.eduarda.literalura.dto.BookDTO;
import com.eduarda.literalura.Repository.AuthorRepository;
import com.eduarda.literalura.Repository.BookRepository;
import com.eduarda.literalura.entity.Author;
import com.eduarda.literalura.entity.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional
    public Book saveBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.title().toLowerCase());
        book.setLanguages(bookDTO.languages());
        book.setDownloadCount(bookDTO.downloadCount());

        List<Author> authors = new ArrayList<>();
        for (AuthorDTO authorDTO : bookDTO.authors()) {
            Author author = new Author();
            author.setName(authorDTO.name().toLowerCase());
            author.setBirthYear(authorDTO.birthYear());
            author.setDeathYear(authorDTO.deathYear());
            authors.add(author);
        }

        authorRepository.saveAll(authors);
        book.setAuthors(authors);

        return bookRepository.save(book);
    }

    @Transactional
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        for (Book book : books) {
            System.out.println(book);
        }

        return books;
    }

    @Transactional
    public void getByName(String name) {
        if (bookRepository.existsByTitle(name)) {
            System.out.println("Este livro não foi encontrado");
            return;
        }
        List<Book> books = bookRepository.findAllByTitle(name).get();

        for (Book book : books) {
            System.out.println(book);
        }
    }

    @Transactional
    public void initBooks() throws JsonProcessingException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://gutendex.com/books/"))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        String responseBody = response.body();

        JsonNode jsonNode = objectMapper.readTree(responseBody);

        JsonNode resultsNode = jsonNode.get("results");

        List<BookDTO> books = objectMapper.convertValue(resultsNode, new TypeReference<List<BookDTO>>() {});

        for (BookDTO bookDTO : books) {
            saveBook(bookDTO);
        }

        System.out.println("Dados salvos no banco de dados!");
    }

    @Transactional
    public void getAuthorByName(String name) {
        if (!authorRepository.existsByName(name)) {
            System.out.println("Autor não encontrado");
            return;
        }

        List<Author> authors = authorRepository.findAllByName(name).get();
        for (Author author : authors) {
            System.out.println(author);
        }
    }

    @Transactional
    public void getAuthorsAliveInYear(String year) {
        List<Author> authors = authorRepository.findAuthorsAliveInYear(Integer.parseInt(year));

        if (authors.isEmpty()) {
            System.out.println("Nenhum autor encontrado");
            return;
        }

        for (Author author : authors) {
            System.out.println(author);
        }
    }

    @Transactional
    public void getBooksByLanguage(String language) {
        List<Book> books = bookRepository.findBooksByLanguage(language.toLowerCase());

        if (books.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma: " + language);
            return;
        }

        for (Book book : books) {
            System.out.println(book);
        }
    }
}
