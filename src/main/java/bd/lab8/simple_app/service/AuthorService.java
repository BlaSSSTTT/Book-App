package bd.lab8.simple_app.service;

import bd.lab8.simple_app.dto.AuthorDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    private final EntityManager entityManager;

    public AuthorService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public List<List<AuthorDto>> executeTopAndBottomSuccessfulAuthors(int genreId) {
        List<List<AuthorDto>> resultTables = new ArrayList<>();

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GetTopAndBottomSuccessfulAuthors");
        query.registerStoredProcedureParameter("GenreId", Integer.class, jakarta.persistence.ParameterMode.IN);
        query.setParameter("GenreId", genreId);

        query.execute();

        List<Object[]> firstTable = query.getResultList();
        List<AuthorDto> topAuthors = mapToAuthorDtoList(firstTable);
        resultTables.add(topAuthors);

        if (query.hasMoreResults()) {
            List<Object[]> secondTable = query.getResultList();
            List<AuthorDto> bottomAuthors = mapToAuthorDtoList(secondTable);
            resultTables.add(bottomAuthors);
        }

        return resultTables;
    }

    private List<AuthorDto> mapToAuthorDtoList(List<Object[]> resultList) {
        List<AuthorDto> authors = new ArrayList<>();
        for (Object[] row : resultList) {
            AuthorDto author = new AuthorDto(
                    (String) row[0],  // author_name
                    ((Number) row[1]).doubleValue(), // total_sales_value
                    ((Number) row[2]).doubleValue(), // total_books_printed
                    ((Number) row[3]).intValue(),    // completed_orders_count
                    ((Number) row[4]).intValue(),    // total_orders_count
                    ((Number) row[5]).doubleValue(), // completed_orders_percentage
                    ((Number) row[6]).intValue(),    // final_rank_by_sales
                    ((Number) row[7]).intValue()     // final_rank_by_quantity
            );
            authors.add(author);
        }
        return authors;
    }
}
