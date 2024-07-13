package com.eduarda.literalura.Repository;

import com.eduarda.literalura.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a WHERE a.name LIKE %:name%")
    Optional<List<Author>> findAllByName(String name);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Author a WHERE a.name LIKE %:name%")
    boolean existsByName(String name);

    @Query("SELECT a FROM Author a WHERE a.birthYear <= :year AND (a.deathYear IS NULL OR a.deathYear >= :year)")
    List<Author> findAuthorsAliveInYear(@Param("year") int year);
}
