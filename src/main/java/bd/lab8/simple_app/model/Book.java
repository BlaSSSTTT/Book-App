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
@Table(name = "Book", schema = "dbo", indexes = {
        @Index(name = "idx_Book_author_id", columnList = "author_id"),
        @Index(name = "idx_Book_genre_id", columnList = "genre_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "UQ__Book__DDDF4BE70AC8953D", columnNames = {"sku"}),
        @UniqueConstraint(name = "UQ__Book__99F9D0A4D10EBAF1", columnNames = {"isbn"})
})
@NamedEntityGraph(name = Book.WITH_GENRE_AND_AUTHOR_GRAPH,
        attributeNodes = {
                @NamedAttributeNode(value = "genre", subgraph = "genre-subgraph"),
                @NamedAttributeNode(value = "author", subgraph = "author-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(name = "genre-subgraph", attributeNodes = @NamedAttributeNode("name")),
                @NamedSubgraph(name = "author-subgraph", attributeNodes = @NamedAttributeNode("name"))
        }
)
public class Book {

    public static final String WITH_GENRE_AND_AUTHOR_GRAPH = "graph.Book.genre.author";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Column(name = "sku")
    private Integer sku;

    @Nationalized
    @Column(name = "isbn", length = 20)
    private String isbn;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Nationalized
    @Column(name = "\"size\"", length = 50)
    private String size;

    @Column(name = "weight")
    private Double weight;

    @Lob
    @Column(name = "annotation")
    private String annotation;

    @ColumnDefault("SYSDATETIMEOFFSET()")  // Правильний варіант для SQL Server
    @Column(name = "create_date_time", updatable = false, insertable = false)
    private Instant createDateTime;

    @Column(name = "update_date_time")
    private Instant updateDateTime;

    @OneToMany(mappedBy = "book")
    private Set<OrderBook> orderBooks = new LinkedHashSet<>();

}