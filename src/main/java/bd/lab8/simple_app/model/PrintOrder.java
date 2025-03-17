package bd.lab8.simple_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "PrintOrder", schema = "dbo", indexes = {
        @Index(name = "idx_Order_customer_id", columnList = "customer_id"),
        @Index(name = "idx_order_rg_date", columnList = "registration_date"),
        @Index(name = "Order_ExecutionDays", columnList = "order_execution_days"),
        @Index(name = "IX_Order_RegistrationDate", columnList = "registration_date"),
        @Index(name = "idx_Order_employee_id", columnList = "employee_id"),
        @Index(name = "idx_Order_order_status_id", columnList = "order_status_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "UQ__Order__FD291E41BE2EBC90", columnNames = {"number"})
})
@NamedEntityGraph(
        name = PrintOrder.WITH_CUSTOMER_EMPLOYEE_STATUS,
        attributeNodes = {
                @NamedAttributeNode(value = "customer", subgraph = "customer-subgraph"),
                @NamedAttributeNode(value = "employee", subgraph = "employee-subgraph"),
                @NamedAttributeNode(value = "orderStatus", subgraph = "status-subgraph"),
                @NamedAttributeNode(value = "printType", subgraph = "pt-subgraph"),
                @NamedAttributeNode(value = "paperType", subgraph = "ppt-subgraph"),
                @NamedAttributeNode(value = "coverType", subgraph = "ct-subgraph"),
                @NamedAttributeNode(value = "fasteningType", subgraph = "ft-subgraph"),

        },
        subgraphs = {
                @NamedSubgraph(name = "customer-subgraph", attributeNodes = @NamedAttributeNode("name")),
                @NamedSubgraph(name = "employee-subgraph", attributeNodes = @NamedAttributeNode("fullName")),
                @NamedSubgraph(name = "status-subgraph", attributeNodes = @NamedAttributeNode("name")),
                @NamedSubgraph(name = "pt-subgraph", attributeNodes = @NamedAttributeNode("name")),
                @NamedSubgraph(name = "ppt-subgraph", attributeNodes = @NamedAttributeNode("name")),
                @NamedSubgraph(name = "ct-subgraph", attributeNodes = @NamedAttributeNode("name")),
                @NamedSubgraph(name = "ft-subgraph", attributeNodes = @NamedAttributeNode("name")),
        }
)
public class PrintOrder {

    public static final String WITH_CUSTOMER_EMPLOYEE_STATUS = "PrintOrder.withCustomerEmployeeStatus";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long id;

    @Column(name = "number", nullable = false)
    private Integer number;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "print_type_id", nullable = false)
    @Enumerated(EnumType.STRING)
    private PrintType printType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paper_type_id", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaperType paperType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cover_type_id", nullable = false)
    @Enumerated(EnumType.STRING)
    private CoverType coverType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fastening_type_id", nullable = false)
    @Enumerated(EnumType.STRING)
    private FasteningType fasteningType;

    @Column(name = "is_laminated", nullable = false)
    private Boolean isLaminated = false;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ColumnDefault("SYSDATETIMEOFFSET()")  // Правильний варіант для SQL Server
    @Column(name = "create_date_time", updatable = false, insertable = false)
    private Instant createDateTime;

    @Column(name = "update_date_time")
    private Instant updateDateTime;

    @Column(name = "order_execution_days")
    private Integer orderExecutionDays;

    @OneToMany(mappedBy = "order")
    private Set<BatchPrint> batchPrints = new LinkedHashSet<>();

    @OneToMany(mappedBy = "order")
    private Set<OrderBook> orderBooks = new LinkedHashSet<>();

    @Transient
    public Integer calculateOrderExecutionDays() {
        if (registrationDate != null && completionDate != null) {
            return (int) ChronoUnit.DAYS.between(registrationDate, completionDate);
        }
        return null;
    }
}/*:3*/