package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
}
