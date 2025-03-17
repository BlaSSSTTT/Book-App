package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
