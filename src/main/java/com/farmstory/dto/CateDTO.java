package com.farmstory.dto;

import com.farmstory.entity.Cate;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateDTO {

    private int cateNo;
    private String cateGroup;
    private String cateName;

    public Cate toEntity() {
        return Cate.builder().cateNo(cateNo).cateName(cateName).build();
    }
}
