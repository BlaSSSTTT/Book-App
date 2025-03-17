package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.FasteningType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FasteningTypeRepository extends JpaRepository<FasteningType, Long> {
}
