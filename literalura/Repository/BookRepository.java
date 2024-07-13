package com.eduarda.literalura.Repository;

import com.eduarda.literalura.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Book b WHERE b.title = :title")
    boolean existsByTitle(String title);

    @Query("SELECT b FROM Book b WHERE b.title LIKE %:title%")
    Optional<List<Book>> findAllByTitle(String title);

    @Query("SELECT b FROM Book b JOIN b.languages lang WHERE LOWER(lang) = LOWER(:language)")
    List<Book> findBooksByLanguage(String language);

}
