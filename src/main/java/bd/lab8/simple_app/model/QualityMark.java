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
@Table(name = "QualityMark", schema = "dbo", uniqueConstraints = {
        @UniqueConstraint(name = "UQ__QualityM__72E12F1B0525C124", columnNames = {"name"})
})
public class QualityMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quality_mark_id", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "name")
    private String name;

    @ColumnDefault("SYSDATETIMEOFFSET()")  // Правильний варіант для SQL Server
    @Column(name = "create_date_time", updatable = false, insertable = false)
    private Instant createDateTime;

    @Column(name = "update_date_time")
    private Instant updateDateTime;

    @OneToMany(mappedBy = "qualityMark")
    private Set<BatchPrint> batchPrints = new LinkedHashSet<>();

}