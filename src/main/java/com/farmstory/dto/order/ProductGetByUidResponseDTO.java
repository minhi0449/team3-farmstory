package com.farmstory.dto.order;

import com.farmstory.entity.Product;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductGetByUidResponseDTO {
    private int prodNo;
    private String prodName;
    private int price;
    private String img;


    public static ProductGetByUidResponseDTO fromEntity(Product product) {
        return ProductGetByUidResponseDTO.builder()
                .prodNo(product.getProdNo())
                .prodName(product.getProdName())
                .price(product.getPrice())
                .img(product.getImg1())
                .build();
    }
}
