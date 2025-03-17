package bd.lab8.simple_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Customer", schema = "dbo", indexes = {
        @Index(name = "idx_Customer_customer_type_id", columnList = "customer_type_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "UQ__Customer__AB6E616422527B19", columnNames = {"email"})
})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_type_id", nullable = false)
    private CustomerType customerType;

    @Nationalized
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address_date")
    private LocalDate addressDate;

    @Nationalized
    @Column(name = "email")
    private String email;

    @ColumnDefault("SYSDATETIMEOFFSET()")  // Правильний варіант для SQL Server
    @Column(name = "create_date_time", updatable = false, insertable = false)
    private Instant createDateTime;

    @Column(name = "update_date_time")
    private Instant updateDateTime;

    @OneToMany(mappedBy = "customer")
    private Set<PrintOrder> orders = new LinkedHashSet<>();

}