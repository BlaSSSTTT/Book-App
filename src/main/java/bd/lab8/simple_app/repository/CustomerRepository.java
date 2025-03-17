package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
