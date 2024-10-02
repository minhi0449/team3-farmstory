package com.farmstory.dto;

import com.farmstory.dto.user.UserDTO;
import com.farmstory.entity.Cart;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {

    private int cartNO;
    private int count;
    private String uid;
    private ProductDTO product;
    private UserDTO user;


    public Cart toEntity(){
        return Cart.builder()
                .cartNo(cartNO)
                .count(count)
                .build();
    }

    public static CartResponseDTO fromEntity(Cart cart){
        return CartResponseDTO.builder()
                .cartNO(cart.getCartNo())
                .count(cart.getCount())
                .build();
    }

}