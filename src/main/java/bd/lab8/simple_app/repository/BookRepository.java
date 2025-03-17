package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.Book;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @NonNull
    @EntityGraph(value = Book.WITH_GENRE_AND_AUTHOR_GRAPH, type = EntityGraph.EntityGraphType.FETCH)
    Page<Book> findAll(@NotNull Pageable pageable);

    @EntityGraph(value = Book.WITH_GENRE_AND_AUTHOR_GRAPH, type = EntityGraph.EntityGraphType.FETCH)
    Page<Book> findByNameContainingIgnoreCase(String searchQuery, Pageable pageable);
}
