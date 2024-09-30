package com.farmstory.dto.article;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDTO {

    private int fno;
    private int ano;
    private String oName;
    private String sName;
    private int download;
    private String rdate;

}
