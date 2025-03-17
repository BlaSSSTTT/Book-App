package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.QualityMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualityMarkRepository extends JpaRepository<QualityMark, Long> {
}
