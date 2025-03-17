package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.CoverType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoverTypeRepository extends JpaRepository<CoverType, Long> {

}