package bd.lab8.simple_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
public class BatchPrintDto {

    private Long id;
    private String number;
    private Integer orderNumber;
    private String qualityMarkName;
    private Integer bookQuantity;
    private LocalDate printDate;
    private String orderStatus;
}