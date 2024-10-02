package com.farmstory.dto.order;

import com.farmstory.dto.ProductDTO;
import com.farmstory.entity.OrderItem;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemsGetByUidResponseDTO {
    private int orderItemNo;

    private int price;
    private int point;
    private int discount;
    private int deliveryfee;
    private int count;
    private OrderGetByUidResponseDTO order;
    private ProductGetByUidResponseDTO product;

    // 엔티티를 DTO로 변환하는 메서드
    public static OrderItemsGetByUidResponseDTO fromEntity(OrderItem orderItem) {
        OrderItemsGetByUidResponseDTO build = OrderItemsGetByUidResponseDTO.builder()
                .orderItemNo(orderItem.getOrderItemNo())
                .price(orderItem.getPrice())
                .point(orderItem.getPoint())
                .discount(orderItem.getDiscount())
                .deliveryfee(orderItem.getDeliveryfee())
                .count(orderItem.getCount())
                .order(orderItem.getOrder() != null ? OrderGetByUidResponseDTO.fromEntity(orderItem.getOrder()):null)
                .product(orderItem.getProduct() != null ? ProductGetByUidResponseDTO.fromEntity(orderItem.getProduct()) : null)
                .build();


        return build;
    }
}
