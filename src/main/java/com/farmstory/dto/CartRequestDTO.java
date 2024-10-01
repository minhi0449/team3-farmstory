package com.farmstory.dto;

import com.farmstory.entity.Cart;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CartRequestDTO {

    private int cartNO;
    private int count;
    private String uid;
    private int product_id;

    private List<Integer> data;


    public Cart toEntity(){
        return Cart.builder()
                .cartNo(cartNO)
                .count(count)
                .build();
    }
}