package com.farmstory.dto;

import lombok.*;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyDTO {
    private int product_id;
    private int count;
    private String uid;
}
