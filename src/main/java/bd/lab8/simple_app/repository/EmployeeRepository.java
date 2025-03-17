package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
