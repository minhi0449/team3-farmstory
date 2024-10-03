package com.farmstory.dto.order;


import com.farmstory.entity.OrderItem;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemResponseDTO {
    private int orderItemNo;

    private int price;
    private int point;
    private int discount;
    private int deliveryfee;
    private int count;

    // 엔티티를 DTO로 변환하는 메서드
    public static OrderItemResponseDTO fromEntity(OrderItem orderItem) {
        return OrderItemResponseDTO.builder()
                .orderItemNo(orderItem.getOrderItemNo())
                .price(orderItem.getPrice())
                .point(orderItem.getPoint())
                .discount(orderItem.getDiscount())
                .deliveryfee(orderItem.getDeliveryfee())
                .count(orderItem.getCount())
                .build();
    }
}
