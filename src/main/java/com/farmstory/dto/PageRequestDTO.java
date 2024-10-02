package com.farmstory.dto;


import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PageRequestDTO {
    @Builder.Default
    private int no =1;  //글번호

    private int cateNo;
    private String cate;

    @Builder.Default
    private int pg =1;  //페이지 번호

    @Builder.Default
    private int size=10; //한페이지에 검색할 갯수

    private String type;
    private String keyword;

    /////////////////////////////////////////
    private String uid; // admin user list
    private int pNo; // product list
    /////////////////////////////////////////

    // 정렬 요소, 페이지 사이즈 매개변수로 넣기
    public Pageable getPageable(String sort,int size) {
        this.size=size;
        return PageRequest.of(this.pg-1,this.size, Sort.by(sort).descending());
    }



}
