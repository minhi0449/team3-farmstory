package com.farmstory.dto.order;

import com.farmstory.entity.Order;
import com.farmstory.entity.OrderItem;
import com.farmstory.enums.PayMethod;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderGetResponseWithPriceDTO {
    private int orderNo;

    private int point;
    private String receiver;
    private String receiverHp;
    private String zip;
    private String addr1;
    private String addr2;
    private PayMethod payMethod;
    private String etc;
    private String createdAt;
    private int totalPrice;

    public static OrderGetResponseWithPriceDTO fromEntity(Order order, int totalPrice) {
        return OrderGetResponseWithPriceDTO.builder()
                .orderNo(order.getOrderNo())
                .point(order.getPoint())
                .receiver(order.getReceiver())
                .receiverHp(order.getReceiverHp())
                .zip(order.getZip())
                .addr1(order.getAddr1())
                .addr2(order.getAddr2())
                .payMethod(order.getPayMethod())
                .etc(order.getEtc())
                .createdAt(order.getCreateAt() != null ? order.getCreateAt().toString() : null)
                .totalPrice(totalPrice)
                .build();
    }

    public static Order toEntity(OrderGetResponseWithPriceDTO dto) {
        return Order.builder()
                .orderNo(dto.getOrderNo())
                .point(dto.getPoint())
                .receiver(dto.getReceiver())
                .receiverHp(dto.getReceiverHp())
                .zip(dto.getZip())
                .addr1(dto.getAddr1())
                .addr2(dto.getAddr2())
                .payMethod(dto.getPayMethod())
                .etc(dto.getEtc())
                .build();
    }
}
