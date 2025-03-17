package bd.lab8.simple_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "OrderBooks", schema = "dbo", indexes = {
        @Index(name = "idx_OrderBooks_order_id", columnList = "order_id"),
        @Index(name = "idx_OrderBooks_book_id", columnList = "book_id")
})
public class OrderBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_books_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private PrintOrder order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "book_quantity", nullable = false)
    private Integer bookQuantity;

    @ColumnDefault("SYSDATETIMEOFFSET()")  // Правильний варіант для SQL Server
    @Column(name = "create_date_time", updatable = false, insertable = false)
    private Instant createDateTime;

    @Column(name = "update_date_time")
    private Instant updateDateTime;

}