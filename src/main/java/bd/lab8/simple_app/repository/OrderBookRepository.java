package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {

    void deleteByOrderId(Long orderId);

    void deleteByBookId(Long bookId);
}
