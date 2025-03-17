package bd.lab8.simple_app.repository;

import bd.lab8.simple_app.dto.BatchPrintDto;
import bd.lab8.simple_app.model.BatchPrint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchPrintRepository extends JpaRepository<BatchPrint, Long> {

    @Query(
            value = "SELECT new bd.lab8.simple_app.dto.BatchPrintDto(" +
                    "bp.id, " +
                    "bp.number, " +
                    "o.number, " +
                    "qm.name, " +
                    "bp.bookQuantity, " +
                    "bp.printDate, " +
                    "os.name) " +
                    "FROM BatchPrint bp " +
                    "JOIN bp.order o " +
                    "JOIN bp.qualityMark qm " +
                    "JOIN o.orderStatus os",
            countQuery = "SELECT COUNT(bp) FROM BatchPrint bp"
    )
    Page<BatchPrintDto> findBatchPrintDetails(Pageable pageable);

    @Query(
            value = "SELECT new bd.lab8.simple_app.dto.BatchPrintDto(" +
                    "bp.id, " +
                    "bp.number, " +
                    "o.number, " +
                    "qm.name, " +
                    "bp.bookQuantity, " +
                    "bp.printDate, " +
                    "os.name) " +
                    "FROM BatchPrint bp " +
                    "JOIN bp.order o " +
                    "JOIN bp.qualityMark qm " +
                    "JOIN o.orderStatus os " +
                    "WHERE bp.number LIKE CONCAT('%', :searchQuery, '%')"
    )
    Page<BatchPrintDto> searchByNumber(String searchQuery, Pageable pageable);

    void deleteByOrderId(Long orderId);
}
