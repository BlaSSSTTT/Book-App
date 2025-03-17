package bd.lab8.simple_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "OrderStatus", schema = "dbo", uniqueConstraints = {
        @UniqueConstraint(name = "UQ__OrderSta__72E12F1BFACD30C1", columnNames = {"name"})
})
public class OrderStatus {
    @Id
    @Column(name = "order_status_id", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "name")
    private String name;

    @ColumnDefault("SYSDATETIMEOFFSET()")  // Правильний варіант для SQL Server
    @Column(name = "create_date_time", updatable = false, insertable = false)
    private Instant createDateTime;

    @Column(name = "update_date_time")
    private Instant updateDateTime;

    @OneToMany(mappedBy = "orderStatus")
    private Set<PrintOrder> orders = new LinkedHashSet<>();

}