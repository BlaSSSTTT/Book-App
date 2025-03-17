package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.PaperType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperTypeRepository extends JpaRepository<PaperType, Long> {
}
