package bd.lab8.simple_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorDto {
    private String authorName;
    private double totalSalesValue;
    private double totalBooksPrinted;
    private int completedOrdersCount;
    private int totalOrdersCount;
    private double completedOrdersPercentage;
    private int finalRankBySales;
    private int finalRankByQuantity;
}
