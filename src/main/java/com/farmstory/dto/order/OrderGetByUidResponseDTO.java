package com.farmstory.dto.order;

import com.farmstory.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderGetByUidResponseDTO {
    private int orderNo;
    private String userName;
    private String createdAt;

    public static OrderGetByUidResponseDTO fromEntity(Order order) {
        return OrderGetByUidResponseDTO.builder()
                .orderNo(order.getOrderNo())
                .userName(order.getUser() != null ? order.getUser().getName() : null)
                .createdAt(order.getCreateAt() != null ? order.getCreateAt().toString() : null)
                .build();
    }
}
