package com.farmstory.dto.article;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDTO {

    private int ano;

    private String group;
    private String cate;

    private String title;
    private String content;
    private int comment;

    private List<MultipartFile> files;

    @Builder.Default
    private int file = 0;

    @Builder.Default
    private int hit = 0;

    private String writer;
    private String regip;
    private String rdate;

    // 추가필드
    private String nick;


    private List<FileDTO> fileList;

    private List<CommentDTO> commentList;

    /*
    Entity 변환 메서드 대신 ModelMapper 사용
    */

}
