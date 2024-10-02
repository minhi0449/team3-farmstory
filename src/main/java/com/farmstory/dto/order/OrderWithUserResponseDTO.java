package com.farmstory.dto.order;

import com.farmstory.dto.user.UserDTO;
import com.farmstory.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderWithUserResponseDTO {
    private int orderNo;
    private String zip;
    private String addr1;
    private String addr2;
    private String payMethod;
    private int point;
    private String receiver;
    private String receiverHp;
    private String etc;
    private String createdAt;
    private UserDTO user;

    public static OrderWithUserResponseDTO fromEntity(Order order) {
        return OrderWithUserResponseDTO.builder()
                .orderNo(order.getOrderNo())
                .zip(order.getZip())
                .addr1(order.getAddr1())
                .addr2(order.getAddr2())
                .payMethod(order.getPayMethod().name())
                .point(order.getPoint())
                .receiver(order.getReceiver())
                .receiverHp(order.getReceiverHp())
                .etc(order.getEtc())
                .createdAt(order.getCreateAt().toString())
                .build();
    }
}
