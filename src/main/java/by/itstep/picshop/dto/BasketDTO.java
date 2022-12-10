package by.itstep.picshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketDTO {
    private Long id;
    private Integer amountPrice;
    private Double sum;
    private List<BasketDetailsDTO> detailsDTOS = new ArrayList<>();

    public void aggregate() {
        this.amountPrice = detailsDTOS.size();
        this.sum = detailsDTOS.stream()
                .map(BasketDetailsDTO::getSum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
