package com.farmstory.dto.order;

import com.farmstory.dto.ProductDTO;
import com.farmstory.entity.OrderItem;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemWithOrderWithProductResponseDTO {
    private int orderItemNo;

    private int price;
    private int point;
    private int discount;
    private int deliveryfee;
    private int count;
    private OrderWithUserResponseDTO order;
    private ProductDTO product;

    // 엔티티를 DTO로 변환하는 메서드
    public static OrderItemWithOrderWithProductResponseDTO fromEntity(OrderItem orderItem) {
        OrderItemWithOrderWithProductResponseDTO build = OrderItemWithOrderWithProductResponseDTO.builder()
                .orderItemNo(orderItem.getOrderItemNo())
                .price(orderItem.getPrice())
                .point(orderItem.getPoint())
                .discount(orderItem.getDiscount())
                .deliveryfee(orderItem.getDeliveryfee())
                .count(orderItem.getCount())
                .build();


        return build;
    }
}
