package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
