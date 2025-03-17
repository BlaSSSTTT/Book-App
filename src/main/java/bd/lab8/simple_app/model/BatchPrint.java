package bd.lab8.simple_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "BatchPrint", schema = "dbo", indexes = {
        @Index(name = "idx_BatchPrint_order_id", columnList = "order_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "UQ__BatchPri__FD291E417F6A5BBE", columnNames = {"number"})
})
public class BatchPrint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_print_id", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "number", nullable = false)
    @NotBlank(message = "Batch number is required")
    private String number;

    @Column(name = "book_quantity", nullable = false)
    @NotNull(message = "Book quantity is required")
    @Min(value = 1, message = "Book quantity must be at least 1")
    private Integer bookQuantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @NotNull(message = "Order is required")
    private PrintOrder order;

    @Column(name = "print_date")
    private LocalDate printDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quality_mark_id", nullable = false)
    @NotNull(message = "Quality mark is required")
    private QualityMark qualityMark;

    @ColumnDefault("SYSDATETIMEOFFSET()")  // Правильний варіант для SQL Server
    @Column(name = "create_date_time", updatable = false, insertable = false)
    private Instant createDateTime;

    @Column(name = "update_date_time")
    private Instant updateDateTime;

}