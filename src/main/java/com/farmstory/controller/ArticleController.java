package com.farmstory.controller;

import com.farmstory.dto.article.ArticleDTO;
import com.farmstory.dto.article.FileDTO;
import com.farmstory.dto.article.PageRequestDTO;
import com.farmstory.dto.article.PageResponseDTO;
import com.farmstory.service.ArticleService;
import com.farmstory.service.article.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final FileService fileService;


    @GetMapping("/article/list/{group}/{cate}")
    public String article(Model model, @PathVariable("group") String group, @PathVariable("cate") String cate,PageRequestDTO pageRequestDTO) {
        log.info("group : "+group+" cate:"+cate);
        model.addAttribute(group,cate);
        PageResponseDTO pageResponseDTO = articleService.selectArticleForListByCate(pageRequestDTO, cate);
        model.addAttribute(pageResponseDTO);

        return "/article/list";
    }
    @GetMapping("/article/write")
    public String write(){

        return "/article/write";
    }

    @PostMapping("/article/write")
    public String write(ArticleDTO articleDTO, HttpServletRequest req){
        String regip = req.getRemoteAddr();
        articleDTO.setRegip(regip);
        log.info(articleDTO);

        // 파일 업로드
        List<FileDTO> uploadedFiles = fileService.uploadFile(articleDTO);

        // 글 저장
        articleDTO.setFile(uploadedFiles.size()); // 첨부 파일 갯수 초기화
        int ano = articleService.insertArticle(articleDTO);

        // 파일 저장
        for(FileDTO fileDTO : uploadedFiles){
            fileDTO.setAno(ano);
            fileService.insertFile(fileDTO);
        }

        return "redirect:/article/list";
    }


    @GetMapping("/article/view")
    public String view(int no, Model model){
        log.info(no);
        ArticleDTO articleDTO = articleService.selectArticle(no);



        log.info(articleDTO);


        model.addAttribute(articleDTO);

        return "/article/view";
    }




    @GetMapping("/article/modify")
    public String modify(){
        return "/article/modify";
    }
}
