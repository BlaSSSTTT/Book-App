package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.PrintType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintTypeRepository extends JpaRepository<PrintType, Long> {
}
