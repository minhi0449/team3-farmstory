package com.farmstory.controller;

import com.farmstory.dto.article.ArticleDTO;
import com.farmstory.dto.article.FileDTO;
import com.farmstory.dto.article.PageRequestDTO;
import com.farmstory.dto.article.PageResponseDTO;
import com.farmstory.service.ArticleService;
import com.farmstory.service.UserService;
import com.farmstory.service.article.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.crypto.interfaces.PBEKey;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final FileService fileService;
    private final UserService userService;


    @GetMapping("/article/list/{group}/{cate}")
    public String article(Model model, @PathVariable("group") String group, @PathVariable("cate") String cate, PageRequestDTO pageRequestDTO) {
        model.addAttribute(group,cate);
        pageRequestDTO.setCate(cate);
        PageResponseDTO pageResponseDTO = articleService.selectArticleAll(pageRequestDTO);
        model.addAttribute(pageResponseDTO);

        return "/article/list";
    }
    @GetMapping("/article/write")
    public String write(Model model,
                        @RequestParam("group") String group,
                        @RequestParam("cate") String cate) {
        model.addAttribute("group", group);
        model.addAttribute("cate", cate);
        return "/article/write";
    }

    @PostMapping("/article/write")
    public String write(ArticleDTO articleDTO, HttpServletRequest req){
        String group = articleDTO.getGroup();
        String cate = articleDTO.getCate();
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

        return "redirect:/article/list"+"/"+group+"/"+cate;
    }


    @GetMapping("/article/view/{group}/{cate}/{ano}")
    public String view(@PathVariable String group, @PathVariable String cate, Model model,@PathVariable int ano) {

        ArticleDTO articleDTO = articleService.selectArticle(ano);


        model.addAttribute("articleDTO",articleDTO);
        model.addAttribute("group", group);
        model.addAttribute("cate", cate); // 필요할 경우 cate도 추가

        return "/article/view";
    }
    @GetMapping("/article/delete")
    public String delete(String group, String cate, @RequestParam("no") int no){

        articleService.deleteArticle(no);
        return "redirect:/article/list"+"/"+group+"/"+cate;
    }

    @GetMapping("/article/modify")
    public String modify(String group, String cate, @RequestParam("no") int no, Model model){
        ArticleDTO articleDTO = articleService.selectArticle(no);
        model.addAttribute("articleDTO",articleDTO);

        model.addAttribute("group", group);
        model.addAttribute("cate", cate);

        return "/article/modify";
    }

}
