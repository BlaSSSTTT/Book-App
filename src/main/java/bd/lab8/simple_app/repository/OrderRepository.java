package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.model.PrintOrder;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<PrintOrder, Long> {

    @NonNull
    @EntityGraph(value = PrintOrder.WITH_CUSTOMER_EMPLOYEE_STATUS, type = EntityGraph.EntityGraphType.FETCH)
    Page<PrintOrder> findAll(@NotNull Pageable pageable);

    @EntityGraph(value = PrintOrder.WITH_CUSTOMER_EMPLOYEE_STATUS, type = EntityGraph.EntityGraphType.FETCH)
    Page<PrintOrder> findByNumberContainingIgnoreCase(String searchQuery, Pageable pageable);
}
