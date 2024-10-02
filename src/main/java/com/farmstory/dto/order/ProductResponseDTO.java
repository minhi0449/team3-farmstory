package com.farmstory.dto.order;

import com.farmstory.entity.Product;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductResponseDTO {
    private int prodNo;
    private String prodName;
    private int deliveryfee;
    private int discount;
    private String etc;
    private String img1;
    private String img2;
    private String img3;
    private int point;
    private int price;
    private String regdate;
    private int stock;
    private String type;

    public static ProductResponseDTO fromEntity(Product product) {
        return ProductResponseDTO.builder()
                .prodNo(product.getProdNo())
                .prodName(product.getProdName())
                .deliveryfee(product.getDeliveryfee())
                .discount(product.getDiscount())
                .etc(product.getEtc())
                .img1(product.getImg1())
                .img2(product.getImg2())
                .img3(product.getImg3())
                .point(product.getPoint())
                .price(product.getPrice())
                .regdate(product.getRegdate())
                .stock(product.getStock())
                .type(product.getType())
                .build();
    }
}
